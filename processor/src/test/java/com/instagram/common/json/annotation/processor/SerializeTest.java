// Copyright 2004-present Facebook. All Rights Reserved.

package com.instagram.common.json.annotation.processor;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.instagram.common.json.annotation.processor.uut.EnumUUT;
import com.instagram.common.json.annotation.processor.uut.EnumUUT__JsonHelper;
import com.instagram.common.json.annotation.processor.uut.MapUUT;
import com.instagram.common.json.annotation.processor.uut.MapUUT__JsonHelper;
import com.instagram.common.json.annotation.processor.uut.SimpleParseUUT;
import com.instagram.common.json.annotation.processor.uut.SimpleParseUUT__JsonHelper;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import org.json.JSONException;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Basic serialization tests.  It depends on deserialization working correctly but that's an
 * acceptable assumption since if deserialization is broken, {@link DeserializeTest} should fail.
 */

public class SerializeTest {
  @Test
  public void simpleSerializeTest() throws IOException, JSONException {
    final int intValue = 25;
    final int integerValue = 37;
    final String stringValue = "hello world\r\n\'\"";
    final List<Integer> integerList = Lists.newArrayList(1, 2, 3, 4);
    final Queue<Integer> integerQueue = Queues.newArrayDeque(Arrays.asList(1, 2, 3, 4));
    final Set<Integer> integerSet = Sets.newHashSet(1, 2, 3, 4);
    final int subIntValue = 30;

    SimpleParseUUT source = new SimpleParseUUT();
    source.intField = intValue;
    source.integerField = integerValue;
    source.stringField = stringValue;
    source.integerListField = integerList;
    source.integerQueueField = integerQueue;
    source.integerSetField = integerSet;
    source.subobjectField = new SimpleParseUUT.SubobjectParseUUT();
    source.subobjectField.intField = subIntValue;

    StringWriter stringWriter = new StringWriter();
    JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);

    SimpleParseUUT__JsonHelper.serializeToJson(jsonGenerator, source, true);
    jsonGenerator.close();

    String inputString = stringWriter.toString();
    JsonParser jp = new JsonFactory().createParser(inputString);
    jp.nextToken();
    SimpleParseUUT parsed = SimpleParseUUT__JsonHelper.parseFromJson(jp);

    assertSame(source.intField, parsed.intField);
    assertEquals(source.integerField, parsed.integerField);
    assertEquals(source.stringField, parsed.stringField);
    assertEquals(source.integerListField, parsed.integerListField);
    // NOTE: this is because ArrayDeque hilariously does not implement .equals()/.hashcode().
    assertEquals(Lists.newArrayList(source.integerQueueField),
        Lists.newArrayList(parsed.integerQueueField));
    assertEquals(source.integerSetField, parsed.integerSetField);
    assertSame(source.subobjectField.intField, parsed.subobjectField.intField);
  }

  @Test
  public void stringSerializeTest() throws IOException {
    final int intValue = 25;
    final int integerValue = 37;
    final String stringValue = "hello world\r\n\'\"";
    final List<Integer> integerList = Lists.newArrayList(1, 2, 3, 4);
    final Queue<Integer> integerQueue = Queues.newArrayDeque(Arrays.asList(1, 2, 3, 4));
    final Set<Integer> integerSet = Sets.newHashSet(1, 2, 3, 4);
    final int subIntValue = 30;

    SimpleParseUUT source = new SimpleParseUUT();
    source.intField = intValue;
    source.integerField = integerValue;
    source.stringField = stringValue;
    source.integerListField = integerList;
    source.integerQueueField = integerQueue;
    source.integerSetField = integerSet;
    source.subobjectField = new SimpleParseUUT.SubobjectParseUUT();
    source.subobjectField.intField = subIntValue;

    String serialized = SimpleParseUUT__JsonHelper.serializeToJson(source);
    SimpleParseUUT parsed = SimpleParseUUT__JsonHelper.parseFromJson(serialized);

    assertSame(source.intField, parsed.intField);
    assertEquals(source.integerField, parsed.integerField);
    assertEquals(source.stringField, parsed.stringField);
    assertEquals(source.integerListField, parsed.integerListField);
    // NOTE: this is because ArrayDeque hilariously does not implement .equals()/.hashcode().
    assertEquals(Lists.newArrayList(source.integerQueueField),
        Lists.newArrayList(parsed.integerQueueField));
    assertEquals(source.integerSetField, parsed.integerSetField);
    assertSame(source.subobjectField.intField, parsed.subobjectField.intField);
  }

  @Test
  public void enumTest() throws IOException {
    final EnumUUT.EnumType value = EnumUUT.EnumType.VALUE3;

    EnumUUT source = new EnumUUT();
    source.enumField = value;

    String serialized = EnumUUT__JsonHelper.serializeToJson(source);
    EnumUUT parsed = EnumUUT__JsonHelper.parseFromJson(serialized);

    assertSame(source.enumField, parsed.enumField);
  }

  @Test
  public void nullObject() throws IOException {
    final int intValue = 25;
    final int integerValue = 37;
    final String stringValue = "hello world\r\n\'\"";
    final List<Integer> integerList = Lists.newArrayList(1, 2, 3, 4);
    final int subIntValue = 30;

    SimpleParseUUT source = new SimpleParseUUT();
    source.intField = intValue;
    // intentionally do not set integerField
    source.stringField = stringValue;
    source.integerListField = integerList;
    source.subobjectField = new SimpleParseUUT.SubobjectParseUUT();
    source.subobjectField.intField = subIntValue;

    String serialized = SimpleParseUUT__JsonHelper.serializeToJson(source);
    SimpleParseUUT parsed = SimpleParseUUT__JsonHelper.parseFromJson(serialized);

    assertSame(source.intField, parsed.intField);
    assertNull(parsed.integerField);
    assertEquals(source.stringField, parsed.stringField);
    assertEquals(source.integerListField, parsed.integerListField);
    assertSame(source.subobjectField.intField, parsed.subobjectField.intField);
  }

  @Test
  public void nullArray() throws IOException {
    final int intValue = 25;
    final int integerValue = 37;
    final String stringValue = "hello world\r\n\'\"";
    final int subIntValue = 30;

    SimpleParseUUT source = new SimpleParseUUT();
    source.intField = intValue;
    source.integerField = integerValue;
    source.stringField = stringValue;
    // intentionally do not set integerListField
    source.subobjectField = new SimpleParseUUT.SubobjectParseUUT();
    source.subobjectField.intField = subIntValue;

    String serialized = SimpleParseUUT__JsonHelper.serializeToJson(source);
    SimpleParseUUT parsed = SimpleParseUUT__JsonHelper.parseFromJson(serialized);

    assertSame(source.intField, parsed.intField);
    assertEquals(source.integerField, parsed.integerField);
    assertEquals(source.stringField, parsed.stringField);
    assertNull(parsed.integerListField);
    assertSame(source.subobjectField.intField, parsed.subobjectField.intField);
  }

  @Test
  public void nullArrayEntry() throws IOException {
    final int intValue = 25;
    final int integerValue = 37;
    final String stringValue = "hello world\r\n\'\"";
    final List<Integer> integerList = Lists.newArrayList(1, 2, 3, null);
    final int subIntValue = 30;

    SimpleParseUUT source = new SimpleParseUUT();
    source.intField = intValue;
    source.integerField = integerValue;
    source.stringField = stringValue;
    source.integerListField = integerList;
    source.subobjectField = new SimpleParseUUT.SubobjectParseUUT();
    source.subobjectField.intField = subIntValue;

    String serialized = SimpleParseUUT__JsonHelper.serializeToJson(source);
    SimpleParseUUT parsed = SimpleParseUUT__JsonHelper.parseFromJson(serialized);

    assertSame(source.intField, parsed.intField);
    assertEquals(source.integerField, parsed.integerField);
    assertEquals(source.stringField, parsed.stringField);
    assertEquals(source.integerListField.size() - 1, parsed.integerListField.size());
    for (int ix = 0; ix < source.integerListField.size() - 1; ix ++) {
      assertEquals(source.integerListField.get(ix), parsed.integerListField.get(ix));
    }
    assertSame(source.subobjectField.intField, parsed.subobjectField.intField);
  }

  @Test
  public void mapTest() throws IOException {
    int integerValue = 1000;
    String stringValue = "hello world\r\n\'\"";

    final HashMap<String, Integer> stringIntegerMap = Maps.newHashMap();
    final HashMap<String, String> stringStringMap = Maps.newHashMap();
    final HashMap<String, Long> stringLongMap = Maps.newHashMap();
    final HashMap<String, Double> stringDoubleMap = Maps.newHashMap();
    final HashMap<String, Float> stringFloatMap = Maps.newHashMap();
    final HashMap<String, Boolean> stringBooleanMap = Maps.newHashMap();
    final HashMap<String, MapUUT.MapObject> stringObjectMap = Maps.newHashMap();

    stringIntegerMap.put(stringValue, integerValue);
    stringIntegerMap.put("key_with_null_value", null);

    stringStringMap.put("key", "100");

    stringLongMap.put("max_long_key", Long.MAX_VALUE);
    stringLongMap.put("min_long_key", Long.MIN_VALUE);

    stringDoubleMap.put("max_double_key", Double.MAX_VALUE);
    stringDoubleMap.put("min_double_key", Double.MIN_VALUE);
    stringDoubleMap.put("nan_double_key", Double.NaN);
    stringDoubleMap.put("neg_infinity", Double.NEGATIVE_INFINITY);
    stringDoubleMap.put("pos_infinity", Double.POSITIVE_INFINITY);

    stringFloatMap.put("max_float_key", Float.MAX_VALUE);
    stringFloatMap.put("min_float_key", Float.MIN_VALUE);

    stringBooleanMap.put("true", Boolean.TRUE);
    stringBooleanMap.put("false", Boolean.FALSE);

    MapUUT.MapObject uut = new MapUUT.MapObject();
    uut.subclassInt = 5;
    stringObjectMap.put("uut", uut);

    MapUUT source = new MapUUT();
    source.stringBooleanMapField = stringBooleanMap;
    source.stringDoubleMapField = stringDoubleMap;
    source.stringFloatMapField = stringFloatMap;
    source.stringIntegerMapField = stringIntegerMap;
    source.stringLongMapField = stringLongMap;
    source.stringObjectMapField = stringObjectMap;
    source.stringStringMapField = stringStringMap;

    String serialized = MapUUT__JsonHelper.serializeToJson(source);
    MapUUT parsed = MapUUT__JsonHelper.parseFromJson(serialized);

    assertEquals(source.stringIntegerMapField, parsed.stringIntegerMapField);
    assertEquals(source.stringStringMapField, parsed.stringStringMapField);
    assertEquals(source.stringBooleanMapField, parsed.stringBooleanMapField);
    assertEquals(source.stringDoubleMapField, parsed.stringDoubleMapField);
    assertEquals(source.stringFloatMapField, parsed.stringFloatMapField);
    assertEquals(source.stringLongMapField, parsed.stringLongMapField);
    assertEquals(source.stringObjectMapField, parsed.stringObjectMapField);
  }
}
