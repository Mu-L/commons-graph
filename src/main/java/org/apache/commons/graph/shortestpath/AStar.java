package org.apache.commons.graph.shortestpath;

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
import static org.apache.commons.graph.utils.Edges.getConnectedVertex;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.commons.graph.DirectedGraph;
import org.apache.commons.graph.Vertex;
import org.apache.commons.graph.WeightedEdge;
import org.apache.commons.graph.WeightedGraph;
import org.apache.commons.graph.WeightedPath;

/**
 * Contains the A* shortest path algorithm implementation.
 */
public final class AStar
{

    /**
     * This class can not be instantiated directly
     */
    private AStar()
    {
        // do nothing
    }

    /**
     * Applies the classical A* algorithm to find the shortest path from the source to the target, if exists.
     *
     * @param <V> the Graph vertices type.
     * @param <WE> the Graph weighted edges type
     * @param graph the Graph which shortest path from {@code source} to {@code target} has to be found
     * @param source the shortest path source Vertex
     * @param target the shortest path target Vertex
     * @param heuristic the <i>h(x)</i> function
     * @return a path which describes the shortest path, if any, otherwise a {@link PathNotFoundException} will be thrown
     */
    public static <V extends Vertex, WE extends WeightedEdge<V>> WeightedPath<V, WE> findShortestPath( WeightedGraph<V, WE> graph,
                                                                                                       V source,
                                                                                                       V target,
                                                                                                       Heuristic<V> heuristic )
    {
        // Cost from start along best known path.
        final ShortestDistances<V> gScores = new ShortestDistances<V>();
        gScores.setWeight( source, 0D );

        final ShortestDistances<V> hScores = new ShortestDistances<V>();
        gScores.setWeight( source, heuristic.applyHeuristic( source, target ) );

        // Estimated total cost from start to goal through y.
        final ShortestDistances<V> fScores = new ShortestDistances<V>();
        fScores.setWeight( source, hScores.getWeight( source ) );

        // The set of nodes already evaluated.
        final Set<V> closedSet = new HashSet<V>();

        // The set of tentative nodes to be evaluated.
        final PriorityQueue<V> openSet = new PriorityQueue<V>( graph.getVertices().size(), fScores );
        openSet.add( source );

        // The of navigated nodes
        final PredecessorsList<V, WE> predecessors = new PredecessorsList<V, WE>();

        // the current node
        V current;

        // extract the node in openset having the lowest f_score[] value
        while ( ( current = openSet.poll() ) != null )
        {
            // destination reached, stop and build the path
            if ( target.equals( current ) )
            {
                return predecessors.buildPath( source, target );
            }

            closedSet.add( current );

            @SuppressWarnings( "unchecked" )
            Set<WE> edges = ( graph instanceof DirectedGraph ) ? ( (DirectedGraph<V, WE>) graph ).getOutbound( current )
                                                               : graph.getEdges( current );
            for ( WE edge : edges )
            {
                V v = getConnectedVertex( current, edge );

                if ( !closedSet.contains( v ) )
                {
                    Double tentativeGScore = gScores.getWeight( current ) + edge.getWeight();

                    if ( openSet.add( v ) || tentativeGScore.compareTo( gScores.getWeight( v ) ) < 0 )
                    {
                        predecessors.addPredecessor( v, edge );
                        gScores.setWeight( v, tentativeGScore );
                        hScores.setWeight( v, heuristic.applyHeuristic( v, target ) );
                        fScores.setWeight( v, gScores.getWeight( v ) + hScores.getWeight( v ) );
                    }
                }
            }
        }

        throw new PathNotFoundException( format( "Path from '%s' to '%s' doesn't exist in Graph '%s'", source, target,
                                                 graph ) );
    }

}