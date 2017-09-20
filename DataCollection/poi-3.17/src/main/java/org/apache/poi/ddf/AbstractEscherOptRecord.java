/*   1:    */ package org.apache.poi.ddf;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Collections;
/*   5:    */ import java.util.Comparator;
/*   6:    */ import java.util.Iterator;
/*   7:    */ import java.util.List;
/*   8:    */ import org.apache.poi.util.LittleEndian;
/*   9:    */ 
/*  10:    */ public abstract class AbstractEscherOptRecord
/*  11:    */   extends EscherRecord
/*  12:    */ {
/*  13: 33 */   private List<EscherProperty> properties = new ArrayList();
/*  14:    */   
/*  15:    */   public void addEscherProperty(EscherProperty prop)
/*  16:    */   {
/*  17: 42 */     this.properties.add(prop);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory)
/*  21:    */   {
/*  22: 49 */     int bytesRemaining = readHeader(data, offset);
/*  23: 50 */     short propertiesCount = readInstance(data, offset);
/*  24: 51 */     int pos = offset + 8;
/*  25:    */     
/*  26: 53 */     EscherPropertyFactory f = new EscherPropertyFactory();
/*  27: 54 */     this.properties = f.createProperties(data, pos, propertiesCount);
/*  28: 55 */     return bytesRemaining + 8;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public List<EscherProperty> getEscherProperties()
/*  32:    */   {
/*  33: 65 */     return this.properties;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public EscherProperty getEscherProperty(int index)
/*  37:    */   {
/*  38: 76 */     return (EscherProperty)this.properties.get(index);
/*  39:    */   }
/*  40:    */   
/*  41:    */   private int getPropertiesSize()
/*  42:    */   {
/*  43: 82 */     int totalSize = 0;
/*  44: 83 */     for (EscherProperty property : this.properties) {
/*  45: 85 */       totalSize += property.getPropertySize();
/*  46:    */     }
/*  47: 88 */     return totalSize;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public int getRecordSize()
/*  51:    */   {
/*  52: 94 */     return 8 + getPropertiesSize();
/*  53:    */   }
/*  54:    */   
/*  55:    */   public <T extends EscherProperty> T lookup(int propId)
/*  56:    */   {
/*  57: 99 */     for (EscherProperty prop : this.properties) {
/*  58:101 */       if (prop.getPropertyNumber() == propId)
/*  59:    */       {
/*  60:104 */         T result = prop;
/*  61:105 */         return result;
/*  62:    */       }
/*  63:    */     }
/*  64:108 */     return null;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public int serialize(int offset, byte[] data, EscherSerializationListener listener)
/*  68:    */   {
/*  69:115 */     listener.beforeRecordSerialize(offset, getRecordId(), this);
/*  70:    */     
/*  71:117 */     LittleEndian.putShort(data, offset, getOptions());
/*  72:118 */     LittleEndian.putShort(data, offset + 2, getRecordId());
/*  73:119 */     LittleEndian.putInt(data, offset + 4, getPropertiesSize());
/*  74:120 */     int pos = offset + 8;
/*  75:121 */     for (EscherProperty property : this.properties) {
/*  76:123 */       pos += property.serializeSimplePart(data, pos);
/*  77:    */     }
/*  78:125 */     for (EscherProperty property : this.properties) {
/*  79:127 */       pos += property.serializeComplexPart(data, pos);
/*  80:    */     }
/*  81:129 */     listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
/*  82:130 */     return pos - offset;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public void sortProperties()
/*  86:    */   {
/*  87:138 */     Collections.sort(this.properties, new Comparator()
/*  88:    */     {
/*  89:    */       public int compare(EscherProperty p1, EscherProperty p2)
/*  90:    */       {
/*  91:143 */         short s1 = p1.getPropertyNumber();
/*  92:144 */         short s2 = p2.getPropertyNumber();
/*  93:145 */         return s1 == s2 ? 0 : s1 < s2 ? -1 : 1;
/*  94:    */       }
/*  95:    */     });
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void setEscherProperty(EscherProperty value)
/*  99:    */   {
/* 100:157 */     Iterator<EscherProperty> iterator = this.properties.iterator();
/* 101:158 */     while (iterator.hasNext())
/* 102:    */     {
/* 103:159 */       EscherProperty prop = (EscherProperty)iterator.next();
/* 104:160 */       if (prop.getId() == value.getId()) {
/* 105:161 */         iterator.remove();
/* 106:    */       }
/* 107:    */     }
/* 108:164 */     this.properties.add(value);
/* 109:165 */     sortProperties();
/* 110:    */   }
/* 111:    */   
/* 112:    */   public void removeEscherProperty(int num)
/* 113:    */   {
/* 114:169 */     for (Iterator<EscherProperty> iterator = getEscherProperties().iterator(); iterator.hasNext();)
/* 115:    */     {
/* 116:170 */       EscherProperty prop = (EscherProperty)iterator.next();
/* 117:171 */       if (prop.getPropertyNumber() == num) {
/* 118:172 */         iterator.remove();
/* 119:    */       }
/* 120:    */     }
/* 121:    */   }
/* 122:    */   
/* 123:    */   protected Object[][] getAttributeMap()
/* 124:    */   {
/* 125:179 */     List<Object> attrList = new ArrayList(this.properties.size() * 2 + 2);
/* 126:180 */     attrList.add("properties");
/* 127:181 */     attrList.add(Integer.valueOf(this.properties.size()));
/* 128:182 */     for (EscherProperty property : this.properties)
/* 129:    */     {
/* 130:183 */       attrList.add(property.getName());
/* 131:184 */       attrList.add(property);
/* 132:    */     }
/* 133:187 */     return new Object[][] { { "isContainer", Boolean.valueOf(isContainerRecord()) }, { "numchildren", Integer.valueOf(getChildRecords().size()) }, attrList.toArray() };
/* 134:    */   }
/* 135:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ddf.AbstractEscherOptRecord
 * JD-Core Version:    0.7.0.1
 */