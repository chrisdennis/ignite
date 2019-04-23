/*
 * Copyright 2019 GridGain Systems, Inc. and Contributors.
 * 
 * Licensed under the GridGain Community Edition License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.gridgain.com/products/software/community-edition/gridgain-community-edition-license
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ignite.internal.processors.cache.mvcc;

import org.apache.ignite.cache.CacheMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.apache.ignite.cache.CacheMode.PARTITIONED;

/** */
@RunWith(JUnit4.class)
public class CacheMvccPartitionedSelectForUpdateQueryTest extends CacheMvccSelectForUpdateQueryAbstractTest {
    /** {@inheritDoc} */
    @Override public CacheMode cacheMode() {
        return PARTITIONED;
    }

    /**
     *
     */
    @Test
    public void testSelectForUpdateDistributedSegmented() throws Exception {
        doTestSelectForUpdateDistributed("PersonSeg", false);
    }

    /**
     *
     */
    @Test
    public void testSelectForUpdateLocalSegmented() throws Exception {
        doTestSelectForUpdateLocal("PersonSeg", false);
    }
}