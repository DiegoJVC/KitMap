package com.cobelpvp.hcfactions.persist.maps;

import com.cobelpvp.hcfactions.persist.PersistMap;
import java.util.UUID;

public class KitmapTokensMap extends PersistMap<Integer> {

    public KitmapTokensMap() {
        super("Tokens", "KitMap.Tokens");
    }

    @Override
    public String getRedisValue(Integer tokens) {
        return (String.valueOf(tokens));
    }

    @Override
    public Integer getJavaObject(String str) {
        return (Integer.parseInt(str));
    }

    @Override
    public Object getMongoValue(Integer tokens) {
        return (tokens);
    }

    public int getTokens(UUID check) {
        return (contains(check) ? getValue(check) : 0);
    }

    public void setTokens(UUID update, int tokens) {
        updateValueAsync(update, tokens);
    }

}