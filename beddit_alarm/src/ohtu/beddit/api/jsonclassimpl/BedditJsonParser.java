package ohtu.beddit.api.jsonclassimpl;

import ohtu.beddit.web.BedditException;

interface BedditJsonParser {

    <T> T parseJsonToObject(String json, Class<T> type) throws BedditException;
}
