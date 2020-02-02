package Keyboards;

public class Nonamekbd {

    public static String NonameKbd(){

        String kbd = "{\n" +
                "  \"one_time\": false,\n" +
                "  \"buttons\": [\n" +
                "      [{\n" +
                "        \"action\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"payload\": \"{\\\"button\\\": \\\"2\\\"}\",\n" +
                "          \"label\": \"Регистрация\"\n" +
                "        },\n" +
                "        \"color\": \"positive\"\n" +
                "      }]\n" +
                "  ]\n" +
                "}";
        return kbd;
    }

}
