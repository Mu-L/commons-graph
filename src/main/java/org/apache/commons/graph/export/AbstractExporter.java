package org.apache.commons.graph.export;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import static java.lang.String.format;
import static org.apache.commons.graph.utils.Assertions.checkNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.graph.Graph;
import org.apache.commons.graph.Mapper;
import org.apache.commons.graph.VertexPair;

abstract class AbstractExporter<V, E, T extends AbstractExporter<V, E, T>>
{

    private static final String G = "G";

    private final Graph<V, E> graph;

    private final Map<String, Mapper<V, ?>> vertexProperties;

    private final Map<String, Mapper<E, ?>> edgeProperties;

    private final String name;

    private Writer writer;

    public AbstractExporter( Graph<V, E> graph, String name )
    {
        this.graph = graph;
        this.writer = null;
        this.vertexProperties = new HashMap<String, Mapper<V, ?>>();
        this.edgeProperties = new HashMap<String, Mapper<E, ?>>();
        this.name = name != null ? name : G;
    }

    protected void addEdgeProperty( String propertyName, Mapper<E, ?> edgeProperty )
    {
        this.edgeProperties.put( propertyName, edgeProperty );
    }

    protected void addVertexProperty( String propertyName, Mapper<V, ?> vertexProperty )
    {
        this.vertexProperties.put( propertyName, vertexProperty );
    }

    protected abstract void comment( String text )
        throws Exception;

    protected abstract void edge( E edge, V head, V tail, Map<String, Object> properties )
        throws Exception;

    protected abstract void endGraph()
        throws Exception;
    
    protected abstract void endSerialization()
        throws Exception;
    
    protected abstract void enlistEdgesProperty( String name, Class<?> type )
        throws Exception;

    protected abstract void enlistVerticesProperty( String name, Class<?> type )
        throws Exception;

    protected final Graph<V, E> getGraph()
    {
        return graph;
    }

    protected final Writer getWriter()
    {
        return writer;
    }

    protected abstract void startGraph( String name )
        throws Exception;

    protected abstract void startSerialization()
        throws Exception;

    public final void to( File outputFile )
        throws GraphExportException
    {
        try
        {
            to( new FileOutputStream( checkNotNull( outputFile, "Impossibe to export the graph in a null file" ) ) );
        }
        catch ( FileNotFoundException e )
        {
            throw new RuntimeException( e );
        }
    }

    public final void to( OutputStream outputStream )
        throws GraphExportException
    {
        to( new OutputStreamWriter( checkNotNull( outputStream, "Impossibe to export the graph in a null stream" ) ) );
    }

    public final void to( Writer writer )
        throws GraphExportException
    {
        this.writer = checkNotNull( writer, "Impossibe to export the graph in a null stream" );

        try
        {
            startSerialization();
            comment( format( "Graph generated by Apache Commons Graph on %s%n", new Date() ) );

            startGraph( name );

            // this is basically for the GraphML



            // END

            for ( V vertex : graph.getVertices() )
            {
                Map<String, Object> properties = new HashMap<String, Object>( vertexProperties.size() );

                for ( Entry<String, Mapper<V, ?>> vertexProperty : vertexProperties.entrySet() )
                {
                    properties.put( vertexProperty.getKey(),
                                    vertexProperty.getValue().map( vertex ) );
                }

                vertex( vertex, properties );
            }

            for ( E edge : graph.getEdges() )
            {
                VertexPair<V> vertices = graph.getVertices( edge );

                Map<String, Object> properties = new HashMap<String, Object>( edgeProperties.size() );

                for ( Entry<String, Mapper<E, ?>> edgeProperty : edgeProperties.entrySet() )
                {
                    properties.put( edgeProperty.getKey(),
                                    edgeProperty.getValue().map( edge ) );
                }

                edge( edge, vertices.getHead(), vertices.getTail(), properties );
            }

            endGraph();

            endSerialization();
        }
        catch ( Exception e )
        {
            throw new GraphExportException( e, "an error occurred while exporting graph %s (named %s) to writer %s",
                                            graph,
                                            name,
                                            writer );
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch ( IOException e )
            {
                // swallow it
            }
        }
    }

    protected abstract void vertex( V vertex, Map<String, Object> properties )
        throws Exception;

}
