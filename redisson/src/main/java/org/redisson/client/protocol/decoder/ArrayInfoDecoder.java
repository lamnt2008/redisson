/**
 * Copyright (c) 2013-2026 Nikita Koksharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.redisson.client.protocol.decoder;

import org.redisson.api.array.ArrayInfo;
import org.redisson.client.handler.State;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Array information decoder.
 *
 * @author lamnt2008
 *
 */
public class ArrayInfoDecoder implements MultiDecoder<ArrayInfo> {

    @Override
    public ArrayInfo decode(List<Object> parts, State state) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i + 1 < parts.size(); i += 2) {
            Object key = parts.get(i);
            Object value = parts.get(i + 1);
            if (key instanceof String && value != null) {
                map.put((String) key, value);
            }
        }

        ArrayInfo info = new ArrayInfo();
        setLong(map, "count", info::setCount);
        setLong(map, "len", info::setLength);
        setLong(map, "next-insert-index", info::setNextInsertIndex);
        setLong(map, "slices", info::setSlices);
        setLong(map, "directory-size", info::setDirectorySize);
        setLong(map, "super-dir-entries", info::setSuperDirectoryEntries);
        setLong(map, "slice-size", info::setSliceSize);
        setLong(map, "dense-slices", info::setDenseSlices);
        setLong(map, "sparse-slices", info::setSparseSlices);
        setDouble(map, "avg-dense-size", info::setAverageDenseSize);
        setDouble(map, "avg-dense-fill", info::setAverageDenseFill);
        setDouble(map, "avg-sparse-size", info::setAverageSparseSize);
        return info;
    }

    private void setLong(Map<String, Object> map, String key, Consumer<Long> setter) {
        Long value = toLong(map.get(key));
        if (value != null) {
            setter.accept(value);
        }
    }

    private void setDouble(Map<String, Object> map, String key, Consumer<Double> setter) {
        Double value = toDouble(map.get(key));
        if (value != null) {
            setter.accept(value);
        }
    }

    private Long toLong(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Double toDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

}
