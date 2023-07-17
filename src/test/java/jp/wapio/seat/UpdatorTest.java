package jp.wapio.seat;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UpdatorTest {

    @Nested
    class sourceがnullの場合 {

        @Nested
        class inputがnullの場合 {

            @Test
            void nullを返す() {
                assertEquals(null, new Updator().update(null, null));
            }
        }

        @Nested
        class inputがMapの場合 {

            @Test
            void inputと同じMapを返す() {
                var input = new HashMap<String, Object>();
                assertEquals(new HashMap<>(), new Updator().update(null, input));
                input.put("String", "String");
                input.put("int", 1);
                input.put("boolean", true);
                input.put("null", null);
                input.put("LocalDateTime", LocalDateTime.now());
                input.put("List", new ArrayList<Object>());
                new HashMap<>().put("Map", new HashMap<>());
                assertEquals(input, new Updator().update(null, input));
            }

        }

    }

    @Nested
    class inputがnullの場合 {

        @Nested
        class sourceがMapの場合 {

            @Test
            void inputと同じMapを返す() {
                var source = new HashMap<String, Object>();
                assertEquals(new HashMap<>(), new Updator().update(null, source));
                source.put("String", "String");
                source.put("int", 1);
                source.put("boolean", true);
                source.put("null", null);
                source.put("LocalDateTime", LocalDateTime.now());
                source.put("List", new ArrayList<Object>());
                new HashMap<>().put("Map", new HashMap<>());
                assertEquals(source, new Updator().update(source, null));
            }

        }

    }

    @Nested
    class Mapの更新 {

        @Test
        void 項目が追加できる() {
            var source = new HashMap<String, Object>();
            var input = new HashMap<String, Object>();
            source.put("1", "11");
            input.put("2", "22");

            var expected = new HashMap<String, Object>();
            expected.put("1", "11");
            expected.put("2", "22");
            assertEquals(expected, new Updator().update(source, input));
        }

        @Test
        void 項目が更新できる() {
            var source = new HashMap<String, Object>();
            var input = new HashMap<String, Object>();
            source.put("1", "11");
            source.put("2", "22");
            input.put("2", "222");

            var expected = new HashMap<String, Object>();
            expected.put("1", "11");
            expected.put("2", "222");
            assertEquals(expected, new Updator().update(source, input));
        }

        @Test
        void Listが更新できる() {
            var source = new HashMap<String, Object>();
            var input = new HashMap<String, Object>();
            var sourceList = new ArrayList<>();
            var inputList = new ArrayList<>();
            sourceList.add("11");
            inputList.add("22");
            source.put("1", sourceList);
            input.put("1", inputList);
            var expected = new HashMap<String, Object>();
            expected.put("1", inputList);
            assertEquals(expected, new Updator().update(source, input));
        }
    }
}
