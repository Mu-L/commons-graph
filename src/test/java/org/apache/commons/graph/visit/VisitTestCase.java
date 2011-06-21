package org.apache.commons.graph.visit;

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

import static org.apache.commons.graph.visit.Visit.breadthFirstSearch;
import static org.apache.commons.graph.visit.Visit.depthFirstSearch;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.graph.Graph;
import org.apache.commons.graph.model.BaseLabeledEdge;
import org.apache.commons.graph.model.BaseLabeledVertex;
import org.apache.commons.graph.model.UndirectedMutableGraph;
import org.junit.Test;

public final class VisitTestCase
{

    /**
     * Graph picture can be see
     * <a href="http://www.personal.kent.edu/~rmuhamma/Algorithms/MyAlgorithms/GraphAlgor/breadthSearch.htm">here</a>
     */
    @Test
    public void verifyBreadthFirstSearch()
    {
        // vertices

        BaseLabeledVertex r = new BaseLabeledVertex( "r" );
        BaseLabeledVertex s = new BaseLabeledVertex( "s" );
        BaseLabeledVertex t = new BaseLabeledVertex( "t" );
        BaseLabeledVertex u = new BaseLabeledVertex( "u" );
        BaseLabeledVertex v = new BaseLabeledVertex( "v" );
        BaseLabeledVertex w = new BaseLabeledVertex( "w" );
        BaseLabeledVertex x = new BaseLabeledVertex( "x" );
        BaseLabeledVertex y = new BaseLabeledVertex( "y" );

        // input graph

        UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledEdge> input =
            new UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledEdge>();

        input.addVertex( r );
        input.addVertex( s );
        input.addVertex( t );
        input.addVertex( u );
        input.addVertex( v );
        input.addVertex( w );
        input.addVertex( x );
        input.addVertex( y );

        input.addEdge( new BaseLabeledEdge( "", s, r ) );
        input.addEdge( new BaseLabeledEdge( "", s, w ) );

        input.addEdge( new BaseLabeledEdge( "", r, v ) );

        input.addEdge( new BaseLabeledEdge( "", w, t ) );
        input.addEdge( new BaseLabeledEdge( "", w, x ) );

        input.addEdge( new BaseLabeledEdge( "", t, u ) );
        input.addEdge( new BaseLabeledEdge( "", t, x ) );

        input.addEdge( new BaseLabeledEdge( "", y, u ) );
        input.addEdge( new BaseLabeledEdge( "", y, x ) );

        // expected graph

        UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledEdge> expected =
            new UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledEdge>();

        for ( BaseLabeledVertex vertex : input.getVertices() )
        {
            expected.addVertex( vertex );
        }

        expected.addEdge( new BaseLabeledEdge( "", s, r ) );
        expected.addEdge( new BaseLabeledEdge( "", s, w ) );
        expected.addEdge( new BaseLabeledEdge( "", r, v ) );
        expected.addEdge( new BaseLabeledEdge( "", w, t ) );
        expected.addEdge( new BaseLabeledEdge( "", w, x ) );
        expected.addEdge( new BaseLabeledEdge( "", t, u ) );
        expected.addEdge( new BaseLabeledEdge( "", y, x ) );

        // actual graph

        Graph<BaseLabeledVertex, BaseLabeledEdge> actual = breadthFirstSearch( input, s );

        // assertion

        assertEquals( expected, actual );
    }

    /**
     * Graph picture can be see
     * <a href="http://aiukswkelasgkelompok7.wordpress.com/metode-pencarian-dan-pelacakan/">here</a>
     */
    @Test
    public void verifyDepthFirstSearch()
    {
        // vertices

        BaseLabeledVertex a = new BaseLabeledVertex( "A" );
        BaseLabeledVertex b = new BaseLabeledVertex( "B" );
        BaseLabeledVertex c = new BaseLabeledVertex( "C" );
        BaseLabeledVertex d = new BaseLabeledVertex( "D" );
        BaseLabeledVertex e = new BaseLabeledVertex( "E" );
        BaseLabeledVertex f = new BaseLabeledVertex( "F" );
        BaseLabeledVertex g = new BaseLabeledVertex( "G" );
        BaseLabeledVertex h = new BaseLabeledVertex( "H" );
        BaseLabeledVertex s = new BaseLabeledVertex( "S" );

        // input graph

        UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledEdge> input =
            new UndirectedMutableGraph<BaseLabeledVertex, BaseLabeledEdge>();

        input.addVertex( a );
        input.addVertex( b );
        input.addVertex( c );
        input.addVertex( d );
        input.addVertex( e );
        input.addVertex( f );
        input.addVertex( g );
        input.addVertex( h );
        input.addVertex( s );

        input.addEdge( new BaseLabeledEdge( "", s, a ) );
        input.addEdge( new BaseLabeledEdge( "", s, b ) );

        input.addEdge( new BaseLabeledEdge( "", a, c ) );
        input.addEdge( new BaseLabeledEdge( "", a, d ) );

        input.addEdge( new BaseLabeledEdge( "", b, e ) );
        input.addEdge( new BaseLabeledEdge( "", b, f ) );

        input.addEdge( new BaseLabeledEdge( "", e, h ) );
        input.addEdge( new BaseLabeledEdge( "", e, g ) );

        // expected node set

        // order is not the same in the pic, due to Stack use
        final List<BaseLabeledVertex> expected = new ArrayList<BaseLabeledVertex>();
        expected.add( s );
        expected.add( b );
        expected.add( f );
        expected.add( e );
        expected.add( g );
        expected.add( h );
        expected.add( a );
        expected.add( d );
        expected.add( c );

        // actual node set

        NodeSequenceVisitor<BaseLabeledVertex, BaseLabeledEdge> visitor =
            new NodeSequenceVisitor<BaseLabeledVertex, BaseLabeledEdge>();
        depthFirstSearch( input, s, visitor );
        final List<BaseLabeledVertex> actual = visitor.getVertices();

        // assertion

        assertEquals( expected, actual );
    }

}