package com.plin.destinyreader.jsonParser.jsonParser

class JSONParser {
    companion object {
        fun hashToId(hash: Long): Long {
            var value = hash
            if (hash.and(2147483648L) != 0L) {
                value = hash - 4294967296
            }
            return value
        }
    }


}

