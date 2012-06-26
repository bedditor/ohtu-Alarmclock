package ohtu.beddit.api.jsonclassimpl;

import ohtu.beddit.web.BedditException;

/**
 * Interface for parsing Json of Beddit API.
 */
interface BedditJsonParser {

    /**
     * This method uses {@link com.google.gson.Gson} to parse given json to an object
     * @param json json to be parsed
     * @param type class of the destination object
     * @return parsed object
     * @throws {@link BedditException} Should the parsing to the destination class object fail miserably.
     */
    <T> T parseJsonToObject(String json, Class<T> type) throws BedditException;
}
