package com.lbd.lgame.redis.tools;

import java.nio.charset.Charset;

import java.util.Optional;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FastJsonRedisSerializer <T> implements RedisSerializer<T>{

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	private Class<T> clazz;
	
	public FastJsonRedisSerializer(Class<T> clazz){
        super();
        this.clazz = clazz;
    }
	
	@Override
	public byte[] serialize(T t) throws SerializationException {
		return Optional.ofNullable(t)
                .map(r -> JSON.toJSONString(r, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET))
                .orElseGet(() -> new byte[0]);
	}

	@Override
	public T deserialize(byte[] bytes) throws SerializationException {
		return Optional.ofNullable(bytes)
                .map(t -> JSON.parseObject(new String(t, DEFAULT_CHARSET), clazz))
                .orElseGet(() -> null);
	}

}
