/*
 * Copyright (c) 2015, 2019 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 2.0, or
 * GNU General Public License version 2, or
 * GNU Lesser General Public License version 2.1.
 */
package org.truffleruby.core.array;

import static org.truffleruby.core.array.ArrayHelpers.setSize;

import org.truffleruby.Layouts;
import org.truffleruby.core.array.library.ArrayStoreLibrary;
import org.truffleruby.language.RubyContextNode;

import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.object.DynamicObject;

@ImportStatic(ArrayGuards.class)
public abstract class ArrayPopOneNode extends RubyContextNode {

    public abstract Object executePopOne(DynamicObject array);

    // Pop from an empty array

    @Specialization(guards = "isEmptyArray(array)")
    protected Object popOneEmpty(DynamicObject array) {
        return nil;
    }

    // Pop from a non-empty array

    @Specialization(guards = "!isEmptyArray(array)", limit = "STORAGE_STRATEGIES")
    protected Object popOne(DynamicObject array,
            @CachedLibrary("getStore(array)") ArrayStoreLibrary stores) {
        final int size = Layouts.ARRAY.getSize(array);
        final Object value = stores.read(Layouts.ARRAY.getStore(array), size - 1);
        setSize(array, size - 1);
        return value;
    }

}
