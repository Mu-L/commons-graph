package org.apache.commons.graph;

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

/**
 * A {@code Path} in a {@link Graph} is a sequence of {@link Vertex} such that from each of its vertices there is an
 * {@link Edge} to the next {@link Vertex} in the sequence.
 *
 * @param <V> the Graph vertices type
 * @param <E> the Graph edges type
 */
public interface Path<V extends Vertex, E extends Edge>
{

    /**
     * Returns the source of the path.
     *
     * @return the source of the path.
     */
    V getSource();

    /**
     * Returns the target of the path.
     *
     * @return the target of the path.
     */
    V getTarget();

    /**
     * Returns a list of Vertices, in order as they go from Start to End.
     *
     * This includes the Start and End vertex, and will have one
     * more entry than the Edges list.
     *
     * @return a list of Vertices, in order as they go from Start to End.
     */
    Iterable<V> getVertices();

    /**
     * Returns the <i>order</i> of a Graph (the number of Vertices);
     *
     * @return the <i>order</i> of a Graph (the number of Vertices);
     */
    int getOrder();

    /**
     * Returns a list of Edges which comprise the path.
     *
     * It will have one less than the list of Vertices.
     *
     * @return a list of Edges which comprise the path.
     */
    Iterable<E> getEdges();

    /**
     * Returns the <i>size</i> of a Graph (the number of Edges)
     *
     * @return the <i>size</i> of a Graph (the number of Edges)
     */
    int getSize();

}
