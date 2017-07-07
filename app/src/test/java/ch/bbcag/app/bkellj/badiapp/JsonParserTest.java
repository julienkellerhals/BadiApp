package ch.bbcag.app.bkellj.badiapp;

import org.json.*;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * Created by bmuelb on 07.07.2017.
 */

public class JsonParserTest {

    @Test
    public void testParseWeather_validataRealJson() throws JSONException {
        String description = "Clear";
        int temp_min = 298;
        int temp_max = 299;
        int sunrise = 1499226141;
        int sunset = 1499282825;

        Date sunriseDate = new Date((long)sunrise*1000);
        Date sunsetDate  = new Date((long)sunset*1000 );

        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        format.setTimeZone(TimeZone.getDefault());

        String sunriseFormated = format.format(sunriseDate);
        String sunsetFormated = format.format(sunsetDate);

        String fakeJson = "{\"coord\":{\"lon\":7.45,\"lat\":46.95},\"weather\":[{\"id\":800,\"main\":\""+description+"\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"base\":\"stations\",\"main\":{\"temp\":298.64,\"pressure\":1020,\"humidity\":47,\"temp_min\":"+temp_min+",\"temp_max\":"+temp_max+"},\"visibility\":10000,\"wind\":{\"speed\":1},\"clouds\":{\"all\":0},\"dt\":1499248200,\"sys\":{\"type\":1,\"id\":6013,\"message\":0.0036,\"country\":\"CH\",\"sunrise\":"+sunrise+",\"sunset\":"+sunset+"},\"id\":2661552,\"name\":\"Bern\",\"cod\":200}";

        List<String> testList = new ArrayList<String>();
        testList.add(description);
        testList.add(String.valueOf(temp_min));
        testList.add(String.valueOf(temp_max));
        testList.add(sunriseFormated);
        testList.add(sunsetFormated);

        JsonParser jsonParser = new JsonParser();
        List<String> actualList = jsonParser.parseWeather(fakeJson);

        assertEquals(testList, actualList);
    }

    //tests is positive if a JSONException is thrown
    @Test(expected = JSONException.class)
    public void testParseWeather_emptyJson() throws JSONException {
        JsonParser jsonParser = new JsonParser();
        jsonParser.parseWeather("");
    }

    @Test
    public void testParseBadiTemp_validateRealJson() throws JSONException {
        String beckenname = "Schwimmbad";
        String temp = "22.0";
        String fakeJson = "\n" +
                "\n" +
                "\n" +
                "\n" +
                "{\n" +
                "    \"badid\": 71,\n" +
                "    \"badname\": \"Schwimmbad\",\n" +
                "    \"kanton\": \"BE\",\n" +
                "    \"plz\": null,\n" +
                "    \"ort\": \"Aarberg\",\n" +
                "    \"adresse1\": null,\n" +
                "    \"adresse2\": null,\n" +
                "    \"email\": null,\n" +
                "    \"telefon\": null,\n" +
                "    \"www\": null,\n" +
                "    \"long\": null,\n" +
                "    \"lat\": null,\n" +
                "    \"zeiten\": null,\n" +
                "    \"preise\": null,\n" +
                "    \"info\": \"Die Messdaten werden von Schweiz Tourismus (<a \\r\\nhref=\\\"http://www.myswitzerland.com\\\" \\r\\ntarget=\\\"_blank\\\">www.myswitzerland.com</a>) zur Verfügung \\r\\ngestellt.\\r\\n\\r\\nAngaben ohne Gewähr.\\r\\n\",\n" +
                "    \"wetterort\": \"Bern\",\n" +
                "    \"uv_station_name\": \"Bern\",\n" +
                "    \"uv_wert\": 5,\n" +
                "    \"uv_date\": \"2017-07-07 00:00:00\",\n" +
                "    \"uv_date_pretty\": \"07.07.\",\n" +
                "    \"becken\": {\n" +
                "        \"Schwimmbecken\": {\n" +
                "            \"beckenid\": 185,\n" +
                "            \"beckenname\": \""+beckenname+"\",\n" +
                "            \"temp\": \""+temp+"\",\n" +
                "            \"date\": \"2017-07-04 08:02:00\",\n" +
                "            \"typ\": \"Freibad\",\n" +
                "            \"status\": \"geöffnet\",\n" +
                "            \"smskeywords\": \";AARBERG;\",\n" +
                "            \"smsname\": \"Aarberg Schwimmbad\",\n" +
                "            \"ismain\": \"T   \",\n" +
                "            \"date_pretty\": \"04.07.\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"bilder\": [],\n" +
                "    \"wetter\": [\n" +
                "        {\n" +
                "            \"wetter_symbol\": 2,\n" +
                "            \"wetter_temp\": \"30.0\",\n" +
                "            \"wetter_date\": \"2017-07-07 00:00:00\",\n" +
                "            \"wetter_date_pretty\": \"07.07.\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"wetter_symbol\": 1,\n" +
                "            \"wetter_temp\": \"31.0\",\n" +
                "            \"wetter_date\": \"2017-07-06 00:00:00\",\n" +
                "            \"wetter_date_pretty\": \"06.07.\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        List<String> fakeList = new ArrayList<>();
        fakeList.add(beckenname+ ": "+temp+" Grad Celsius");

        JsonParser jsonParser = new JsonParser();
        assertEquals(fakeList, jsonParser.parseBadiTemp(fakeJson));
    }

    //tests is positive if a JSONException is thrown
    @Test(expected = JSONException.class)
    public void testParseBadiTemp_validateEmtpyString() throws JSONException {
        JsonParser jsonParser = new JsonParser();
        jsonParser.parseBadiTemp("");
    }
}
