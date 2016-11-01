/**
 * 
 */
package com.giants.web.springmvc.json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;

/**
 * spring mvc 4.2 以上版本使用，spring mvc 4.2以下版本请使用fastjson官方JSON转换器
 * @author vencent.lu
 *
 */
public class FastJsonHttpMessageConverter extends
		AbstractGenericHttpMessageConverter<Object> implements
		GenericHttpMessageConverter<Object> {

	private Charset charset = IOUtils.UTF8;

	private SerializerFeature[] features = new SerializerFeature[0];
	
	private JsonProcessInterceptor[] jsonProcessInterceptors;

	protected SerializeFilter[] filters = new SerializeFilter[0];

	protected String dateFormat;

	public FastJsonHttpMessageConverter() {

		super(MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED);
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return true;
	}

	public Charset getCharset() {
		return this.charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public SerializerFeature[] getFeatures() {
		return features;
	}

	public void setFeatures(SerializerFeature... features) {
		this.features = features;
	}

	public SerializeFilter[] getFilters() {
		return filters;
	}

	public void setFilters(SerializeFilter... filters) {
		this.filters = filters;
	}
	
	public void setJsonProcessInterceptors(
			JsonProcessInterceptor[] jsonProcessInterceptors) {
		this.jsonProcessInterceptors = jsonProcessInterceptors;
	}
	
	public void setJsonProcessInterceptor(
			JsonProcessInterceptor jsonProcessInterceptor) {
		this.jsonProcessInterceptors = new JsonProcessInterceptor[]{jsonProcessInterceptor};
	}
	
	private void readBefore(byte[] jsonBytes) {
		if (ArrayUtils.isNotEmpty(this.jsonProcessInterceptors)) {
			for (JsonProcessInterceptor interceptor : this.jsonProcessInterceptors) {
				interceptor.readBefore(jsonBytes);
			}
		}
	}
	
	private void readAfter(Object object) {
		if (ArrayUtils.isNotEmpty(this.jsonProcessInterceptors)) {
			for (JsonProcessInterceptor interceptor : this.jsonProcessInterceptors) {
				interceptor.readAfter(object);
			}
		}
	}
	
	private void writeBefore(Object object) {
		if (ArrayUtils.isNotEmpty(this.jsonProcessInterceptors)) {
			for (JsonProcessInterceptor interceptor : this.jsonProcessInterceptors) {
				interceptor.writeBefore(object);
			}
		}
	}
	
	private void writeAfter(String jsonStr) {
		if (ArrayUtils.isNotEmpty(this.jsonProcessInterceptors)) {
			for (JsonProcessInterceptor interceptor : this.jsonProcessInterceptors) {
				interceptor.writeAfter(jsonStr);
			}
		}
	}

	@Override
	protected Object readInternal(Class<? extends Object> clazz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		InputStream in = inputMessage.getBody();

		byte[] buf = new byte[1024];
		for (;;) {
			int len = in.read(buf);
			if (len == -1) {
				break;
			}

			if (len > 0) {
				baos.write(buf, 0, len);
			}
		}

		byte[] bytes = baos.toByteArray();
		this.readBefore(bytes);
		Object obj = JSON.parseObject(bytes, 0, bytes.length,
				charset.newDecoder(), clazz);
		this.readAfter(obj);
		return obj;
	}

	protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		HttpHeaders headers = outputMessage.getHeaders();
		String text = null;
		this.writeBefore(obj);
		if (!(obj instanceof JsonpResult)) {
			text = JSON.toJSONString(obj, //
					SerializeConfig.globalInstance, //
					filters, //
					dateFormat, //
					JSON.DEFAULT_GENERATE_FEATURE, //
					features);			
		} else {
			JsonpResult jsonp = (JsonpResult) obj;
			text = new StringBuilder(jsonp.getJsonpFunction())
					.append('(')
					.append(JSON.toJSONString(
							jsonp.getValue(), //
							SerializeConfig.globalInstance,
							filters,
							dateFormat,
							JSON.DEFAULT_GENERATE_FEATURE,
							features))
					.append(");").toString();
		}
		this.writeAfter(text);
		byte[] bytes = text.getBytes(charset);
		headers.setContentLength(bytes.length);
		OutputStream out = outputMessage.getBody();
		out.write(bytes);
		out.flush();
	}

	public void addSerializeFilter(SerializeFilter filter) {
		if (filter == null) {
			return;
		}

		SerializeFilter[] filters = new SerializeFilter[this.filters.length + 1];
		System.arraycopy(this.filters, 0, filter, 0, this.filters.length);
		filters[filters.length - 1] = filter;
		this.filters = filters;
	}

	@Override
	public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {

		return super.canRead(type.getClass(), mediaType);
	}

	@Override
	public Object read(Type type, Class<?> contextClass,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		InputStream in = inputMessage.getBody();

		byte[] buf = new byte[1024];
		for (;;) {
			int len = in.read(buf);
			if (len == -1) {
				break;
			}

			if (len > 0) {
				baos.write(buf, 0, len);
			}
		}

		byte[] bytes = baos.toByteArray();
		this.readBefore(bytes);
		Object obj = JSON.parseObject(bytes, 0, bytes.length,
				charset.newDecoder(), type);
		this.readAfter(obj);
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.http.converter.AbstractGenericHttpMessageConverter
	 * #writeInternal(java.lang.Object, java.lang.reflect.Type,
	 * org.springframework.http.HttpOutputMessage)
	 */
	@Override
	protected void writeInternal(Object obj, Type type,
			HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {
		this.writeInternal(obj, outputMessage);

	}

}
