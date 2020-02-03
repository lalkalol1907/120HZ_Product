package Keyboards;

public class PupilKeyboard {

    public static String PupilKbd(){

        String kbd = "{\n" +
                "  \"one_time\": false,\n" +
                "  \"buttons\": [\n" +
                "      [{\n" +
                "        \"action\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"payload\": \"{\\\"button\\\": \\\"1\\\"}\",\n" +
                "          \"label\": \"Подписаться на активность\"\n" +
                "        },\n" +
                "        \"color\": \"positive\"\n" +
                "      }],\n" +
                "      [{\n" +
                "        \"action\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
                "          \"label\": \"Покажи мои активности\"\n" +
                "        },\n" +
                "        \"color\": \"positive\"\n" +
                "      }],\n" +
                "      [{\n" +
                "        \"action\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
                "          \"label\": \"Отписаться от активности\"\n" +
                "        },\n" +
                "        \"color\": \"negative\"\n" +
                "      }]\n" +
                "  ]\n" +
                "}";
        return kbd;
    }
}
