/*
 * Copyright (c) 2016, 2019 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 2.0, or
 * GNU General Public License version 2, or
 * GNU Lesser General Public License version 2.1.
 */
package org.truffleruby.core.array;

import org.truffleruby.Layouts;
import org.truffleruby.core.array.library.ArrayStoreLibrary;
import org.truffleruby.language.RubyContextNode;
import org.truffleruby.language.RubyGuards;

import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.object.DynamicObject;

@ImportStatic(ArrayGuards.class)
public abstract class ArrayToObjectArrayNode extends RubyContextNode {

    public static ArrayToObjectArrayNode create() {
        return ArrayToObjectArrayNodeGen.create();
    }

    public Object[] unsplat(Object[] arguments) {
        assert arguments.length == 1;
        assert RubyGuards.isRubyArray(arguments[0]);
        return executeToObjectArray((DynamicObject) arguments[0]);
    }

    public abstract Object[] executeToObjectArray(DynamicObject array);

    @Specialization(limit = "STORAGE_STRATEGIES")
    protected Object[] toObjectArrayOther(DynamicObject array,
            @CachedLibrary("getStore(array)") ArrayStoreLibrary stores) {
        final int size = Layouts.ARRAY.getSize(array);
        final Object store = Layouts.ARRAY.getStore(array);
        return stores.boxedCopyOfRange(store, 0, size);
    }

}
