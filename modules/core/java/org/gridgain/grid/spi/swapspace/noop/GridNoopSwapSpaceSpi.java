/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.spi.swapspace.noop;

import org.gridgain.grid.lang.*;
import org.gridgain.grid.logger.*;
import org.gridgain.grid.resources.*;
import org.gridgain.grid.spi.*;
import org.gridgain.grid.spi.swapspace.*;
import org.gridgain.grid.util.*;
import org.gridgain.grid.util.typedef.internal.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * No-op implementation of {@link GridSwapSpaceSpi}. Exists for testing and benchmarking purposes.
 *
 * @author @java.author
 * @version @java.version
 */
@GridSpiInfo(
    author = /*@java.spi.author*/"GridGain Systems",
    url = /*@java.spi.url*/"www.gridgain.com",
    email = /*@java.spi.email*/"support@gridgain.com",
    version = /*@java.spi.version*/"x.x")
@GridSpiNoop
@GridSpiMultipleInstancesSupport(true)
public class GridNoopSwapSpaceSpi extends GridSpiAdapter implements GridSwapSpaceSpi {
    /** Logger. */
    @GridLoggerResource
    private GridLogger log;

    /** {@inheritDoc} */
    @Override public void spiStart(@Nullable String gridName) throws GridSpiException {
        U.warn(log, "Swap space is disabled. To enable use GridFileSwapSpaceSpi or GridLevelDbSwapSpaceSpi " +
            "(must have Microsoft Visual C++ Redistributable Package installed).",
            "Swap space is disabled (to enable use GridFileSwapSpaceSpi or GridLevelDbSwapSpaceSpi).");
    }

    /** {@inheritDoc} */
    @Override public void spiStop() throws GridSpiException {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public void clear(@Nullable String space) throws GridSpiException {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public long size(@Nullable String space) throws GridSpiException {
        return 0;
    }

    /** {@inheritDoc} */
    @Override public long count(@Nullable String space) throws GridSpiException {
        return 0;
    }

    /** {@inheritDoc} */
    @Override @Nullable public byte[] read(@Nullable String spaceName, GridSwapKey key, GridSwapContext ctx)
        throws GridSpiException {
        return null;
    }

    /** {@inheritDoc} */
    @Override public Map<GridSwapKey, byte[]> readAll(@Nullable String spaceName, Iterable<GridSwapKey> keys,
        GridSwapContext ctx) throws GridSpiException {
        return Collections.emptyMap();
    }

    /** {@inheritDoc} */
    @Override public void remove(@Nullable String spaceName, GridSwapKey key, @Nullable GridInClosure<byte[]> c,
        GridSwapContext ctx) throws GridSpiException {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public void removeAll(@Nullable String spaceName, Collection<GridSwapKey> keys,
        @Nullable GridBiInClosure<GridSwapKey, byte[]> c, GridSwapContext ctx) throws GridSpiException {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public void store(@Nullable String spaceName, GridSwapKey key, @Nullable byte[] val,
        GridSwapContext ctx) throws GridSpiException {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public void storeAll(@Nullable String spaceName, Map<GridSwapKey, byte[]> pairs,
        GridSwapContext ctx) throws GridSpiException {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public void setListener(@Nullable GridSwapSpaceSpiListener evictLsnr) {
        // No-op.
    }

    /** {@inheritDoc} */
    @Override public Collection<Integer> partitions(@Nullable String spaceName) throws GridSpiException {
        return Collections.emptyList();
    }

    /** {@inheritDoc} */
    @Override public <K> GridSpiCloseableIterator<K> keyIterator(@Nullable String spaceName,
        GridSwapContext ctx) throws GridSpiException {
        return new GridEmptyCloseableIterator<>();
    }

    /** {@inheritDoc} */
    @Override public GridSpiCloseableIterator<Map.Entry<byte[], byte[]>> rawIterator(
        @Nullable String spaceName) throws GridSpiException {
        return new GridEmptyCloseableIterator<>();
    }

    /** {@inheritDoc} */
    @Override public GridSpiCloseableIterator<Map.Entry<byte[], byte[]>> rawIterator(@Nullable String spaceName,
        int part) throws GridSpiException {
        return new GridEmptyCloseableIterator<>();
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridNoopSwapSpaceSpi.class, this);
    }
}
