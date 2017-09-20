/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Constructor;
/*   4:    */ import java.lang.reflect.Field;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.Map;
/*   7:    */ import org.apache.poi.util.LittleEndian;
/*   8:    */ 
/*   9:    */ public class DefaultEscherRecordFactory
/*  10:    */   implements EscherRecordFactory
/*  11:    */ {
/*  12: 32 */   private static Class<?>[] escherRecordClasses = { EscherBSERecord.class, EscherOptRecord.class, EscherTertiaryOptRecord.class, EscherClientAnchorRecord.class, EscherDgRecord.class, EscherSpgrRecord.class, EscherSpRecord.class, EscherClientDataRecord.class, EscherDggRecord.class, EscherSplitMenuColorsRecord.class, EscherChildAnchorRecord.class, EscherTextboxRecord.class };
/*  13: 39 */   private static Map<Short, Constructor<? extends EscherRecord>> recordsMap = recordsToMap(escherRecordClasses);
/*  14:    */   
/*  15:    */   public EscherRecord createRecord(byte[] data, int offset)
/*  16:    */   {
/*  17: 50 */     short options = LittleEndian.getShort(data, offset);
/*  18: 51 */     short recordId = LittleEndian.getShort(data, offset + 2);
/*  19: 58 */     if (isContainer(options, recordId))
/*  20:    */     {
/*  21: 59 */       EscherContainerRecord r = new EscherContainerRecord();
/*  22: 60 */       r.setRecordId(recordId);
/*  23: 61 */       r.setOptions(options);
/*  24: 62 */       return r;
/*  25:    */     }
/*  26: 65 */     if ((recordId >= -4072) && (recordId <= -3817))
/*  27:    */     {
/*  28:    */       EscherBlipRecord r;
/*  29:    */       EscherBlipRecord r;
/*  30: 68 */       if ((recordId == -4065) || (recordId == -4067) || (recordId == -4066))
/*  31:    */       {
/*  32: 72 */         r = new EscherBitmapBlip();
/*  33:    */       }
/*  34:    */       else
/*  35:    */       {
/*  36:    */         EscherBlipRecord r;
/*  37: 74 */         if ((recordId == -4070) || (recordId == -4069) || (recordId == -4068)) {
/*  38: 78 */           r = new EscherMetafileBlip();
/*  39:    */         } else {
/*  40: 80 */           r = new EscherBlipRecord();
/*  41:    */         }
/*  42:    */       }
/*  43: 82 */       r.setRecordId(recordId);
/*  44: 83 */       r.setOptions(options);
/*  45: 84 */       return r;
/*  46:    */     }
/*  47: 87 */     Constructor<? extends EscherRecord> recordConstructor = (Constructor)recordsMap.get(Short.valueOf(recordId));
/*  48: 89 */     if (recordConstructor == null) {
/*  49: 90 */       return new UnknownEscherRecord();
/*  50:    */     }
/*  51:    */     EscherRecord escherRecord;
/*  52:    */     try
/*  53:    */     {
/*  54: 93 */       escherRecord = (EscherRecord)recordConstructor.newInstance(new Object[0]);
/*  55:    */     }
/*  56:    */     catch (Exception e)
/*  57:    */     {
/*  58: 95 */       return new UnknownEscherRecord();
/*  59:    */     }
/*  60: 97 */     escherRecord.setRecordId(recordId);
/*  61: 98 */     escherRecord.setOptions(options);
/*  62: 99 */     return escherRecord;
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected static Map<Short, Constructor<? extends EscherRecord>> recordsToMap(Class<?>[] recClasses)
/*  66:    */   {
/*  67:111 */     Map<Short, Constructor<? extends EscherRecord>> result = new HashMap();
/*  68:112 */     Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
/*  69:114 */     for (Class<?> recClass : recClasses)
/*  70:    */     {
/*  71:116 */       Class<? extends EscherRecord> recCls = recClass;
/*  72:    */       short sid;
/*  73:    */       try
/*  74:    */       {
/*  75:119 */         sid = recCls.getField("RECORD_ID").getShort(null);
/*  76:    */       }
/*  77:    */       catch (IllegalArgumentException e)
/*  78:    */       {
/*  79:121 */         throw new RuntimeException(e);
/*  80:    */       }
/*  81:    */       catch (IllegalAccessException e)
/*  82:    */       {
/*  83:123 */         throw new RuntimeException(e);
/*  84:    */       }
/*  85:    */       catch (NoSuchFieldException e)
/*  86:    */       {
/*  87:125 */         throw new RuntimeException(e);
/*  88:    */       }
/*  89:    */       Constructor<? extends EscherRecord> constructor;
/*  90:    */       try
/*  91:    */       {
/*  92:129 */         constructor = recCls.getConstructor(EMPTY_CLASS_ARRAY);
/*  93:    */       }
/*  94:    */       catch (NoSuchMethodException e)
/*  95:    */       {
/*  96:131 */         throw new RuntimeException(e);
/*  97:    */       }
/*  98:133 */       result.put(Short.valueOf(sid), constructor);
/*  99:    */     }
/* 100:135 */     return result;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public static boolean isContainer(short options, short recordId)
/* 104:    */   {
/* 105:139 */     if ((recordId >= -4096) && (recordId <= -4091)) {
/* 106:141 */       return true;
/* 107:    */     }
/* 108:143 */     if (recordId == -4083) {
/* 109:144 */       return false;
/* 110:    */     }
/* 111:146 */     return (options & 0xF) == 15;
/* 112:    */   }
/* 113:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.DefaultEscherRecordFactory
 * JD-Core Version:    0.7.0.1
 */