/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.cache.datastructures;

import org.gridgain.grid.*;
import org.jetbrains.annotations.*;

/**
 * This runtime exception gets thrown if attempt to access an invalid data structure has been made.
 * Data structure may become invalid if communication with remote nodes has been lost or
 * any other error condition happened that prevented from insuring consistent state.
 * <p>
 * The best way to handle this error is to discard the invalid data structure instance and try
 * getting the underlying data structure from cache again.
 * <p>
 * Note that data structures throw runtime exceptions out of methods that don't have
 * checked exceptions in the signature.
 *
 * @author @java.author
 * @version @java.version
 */
public class GridCacheDataStructureInvalidRuntimeException extends GridRuntimeException {
    /**
     * Creates new exception with given error message.
     *
     * @param msg Error message.
     */
    public GridCacheDataStructureInvalidRuntimeException(String msg) {
        super(msg);
    }

    /**
     * Creates new exception with given throwable as a nested cause and
     * source of error message.
     *
     * @param cause Non-null throwable cause.
     */
    public GridCacheDataStructureInvalidRuntimeException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

    /**
     * Creates a new exception with given error message and optional nested cause exception.
     *
     * @param msg Error message.
     * @param cause Optional nested exception (can be {@code null}).
     */
    public GridCacheDataStructureInvalidRuntimeException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
