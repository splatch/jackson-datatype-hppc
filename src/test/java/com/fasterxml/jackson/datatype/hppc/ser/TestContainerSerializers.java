package com.fasterxml.jackson.datatype.hppc.ser;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.carrotsearch.hppc.*;

import com.fasterxml.jackson.datatype.hppc.HppcTestBase;

public class TestContainerSerializers extends HppcTestBase
{
    public void testByteSerializer() throws Exception
    {
        ObjectMapper mapper = mapperWithModule();

        final byte[] input = new byte[] {
                (byte)-12, (byte)0, (byte) -1, (byte) 0x7F
        };
        ByteArrayList array = new ByteArrayList();
        array.add(input);
        // will be base64 encoded
        
        assertEquals(mapper.writeValueAsString(input), mapper.writeValueAsString(array));

        ByteOpenHashSet set = new ByteOpenHashSet();
        set.add(new byte[] { (byte) 1, (byte) 2});
        String str = mapper.writeValueAsString(set);
        // hmmh. order of set is indetermined, so this may not be stable...
        assertEquals("\"AgE=\"", str);
    }

    public void testShortSerializer() throws Exception
    {
        ObjectMapper mapper = mapperWithModule();

        ShortArrayList array = new ShortArrayList();
        array.add((short)-12, (short)0);
        assertEquals("[-12,0]", mapper.writeValueAsString(array));

        ShortOpenHashSet set = new ShortOpenHashSet();
        set.add((short)1, (short)2);
        String str = mapper.writeValueAsString(set);
        if (!"[1,2]".equals(str) && !"[2,1]".equals(str)) {
            fail("Incorrect serialization: "+str);
        }
    }

    public void testIntSerializer() throws Exception
    {
        ObjectMapper mapper = mapperWithModule();

        IntArrayList array = new IntArrayList();
        array.add(-12, 0);
        assertEquals("[-12,0]", mapper.writeValueAsString(array));

        IntOpenHashSet set = new IntOpenHashSet();
        set.add(1, 2);
        String str = mapper.writeValueAsString(set);
        if (!"[1,2]".equals(str) && !"[2,1]".equals(str)) {
            fail("Incorrect serialization: "+str);
        }
    }

    public void testLongSerializer() throws Exception
    {
        ObjectMapper mapper = mapperWithModule();

        LongArrayList array = new LongArrayList();
        array.add(-12L, 0L);
        assertEquals("[-12,0]", mapper.writeValueAsString(array));

        LongOpenHashSet set = new LongOpenHashSet();
        set.add(1L, 2L);
        String str = mapper.writeValueAsString(set);
        if (!"[1,2]".equals(str) && !"[2,1]".equals(str)) {
            fail("Incorrect serialization: "+str);
        }
    }

    public void testCharSerializer() throws Exception
    {
        ObjectMapper mapper = mapperWithModule();

        CharArrayList array = new CharArrayList();
        array.add('a', 'b', 'c');
        assertEquals("\"abc\"", mapper.writeValueAsString(array));

        CharOpenHashSet set = new CharOpenHashSet();
        set.add('d','e');
        String str = mapper.writeValueAsString(set);
        if (!"[\"de\"]".equals(str) && !"\"ed\"".equals(str)) {
            fail("Incorrect serialization: "+str);
        }
    }

    public void testFloatSerializer() throws Exception
    {
        ObjectMapper mapper = mapperWithModule();
        FloatArrayList array = new FloatArrayList();
        array.add(-12.25f, 0.5f);
        assertEquals("[-12.25,0.5]", mapper.writeValueAsString(array));

        FloatOpenHashSet set = new FloatOpenHashSet();
        set.add(1.75f, 0.5f);
        String str = mapper.writeValueAsString(set);
        if (!"[1.75,0.5]".equals(str) && !"[0.5,1.75]".equals(str)) {
            fail("Incorrect serialization: "+str);
        }
    }

    public void testDoubleSerializer() throws Exception
    {
        ObjectMapper mapper = mapperWithModule();
        DoubleArrayList array = new DoubleArrayList();
        array.add(-12.25, 0.5);
        assertEquals("[-12.25,0.5]", mapper.writeValueAsString(array));

        DoubleOpenHashSet set = new DoubleOpenHashSet();
        set.add(1.75, 0.5);
        String str = mapper.writeValueAsString(set);
        if (!"[1.75,0.5]".equals(str) && !"[0.5,1.75]".equals(str)) {
            fail("Incorrect serialization: "+str);
        }
    }

    /*
    /**********************************************************************
    /* Tests for non-numeric containers
    /**********************************************************************
     */

    public void testObjectContainerSerializer() throws Exception
    {
        ObjectMapper mapper = mapperWithModule();
        // first, untyped case
        ObjectArrayList<Object> list = new ObjectArrayList<Object>();
        list.add("foo");
        list.add(Integer.valueOf(3));
        list.add((Object) null);
        assertEquals("[\"foo\",3,null]", mapper.writeValueAsString(list));

        // TODO: polymorphic case (@JsonTypeInfo and/or default typing)
    }
    
    public void testBitSetSerializer() throws Exception
    {
        ObjectMapper mapper = mapperWithModule();
        BitSet bitset = new BitSet();
        bitset.set(1);
        bitset.set(4);

        // note: since storage is in units of 64 bits, we may get more than what we asked for, so:
        String str = mapper.writeValueAsString(bitset);
        assertTrue(str.startsWith("[false,true,false,false,true"));
    }
}
