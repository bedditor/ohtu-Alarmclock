package ohtu.beddit.api.jsonclassimpl;

import ohtu.beddit.web.BedditException;

interface BedditJsonParser {

    /**
     * This method uses {@link com.google.gson.Gson#fromJson(com.google.gson.JsonElement, Class)} to parse given json to an object
     * @param json json to be parsed
     * @param type class of the destination object
     * @return parsed object
     * @throws BedditException
     */
    <T> T parseJsonToObject(String json, Class<T> type) throws BedditException;
}
