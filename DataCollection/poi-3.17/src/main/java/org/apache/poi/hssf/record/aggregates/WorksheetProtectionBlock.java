/*   1:    */ package org.apache.poi.hssf.record.aggregates;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.model.RecordStream;
/*   4:    */ import org.apache.poi.hssf.record.ObjectProtectRecord;
/*   5:    */ import org.apache.poi.hssf.record.PasswordRecord;
/*   6:    */ import org.apache.poi.hssf.record.ProtectRecord;
/*   7:    */ import org.apache.poi.hssf.record.Record;
/*   8:    */ import org.apache.poi.hssf.record.ScenarioProtectRecord;
/*   9:    */ import org.apache.poi.poifs.crypt.CryptoFunctions;
/*  10:    */ import org.apache.poi.util.RecordFormatException;
/*  11:    */ 
/*  12:    */ public final class WorksheetProtectionBlock
/*  13:    */   extends RecordAggregate
/*  14:    */ {
/*  15:    */   private ProtectRecord _protectRecord;
/*  16:    */   private ObjectProtectRecord _objectProtectRecord;
/*  17:    */   private ScenarioProtectRecord _scenarioProtectRecord;
/*  18:    */   private PasswordRecord _passwordRecord;
/*  19:    */   
/*  20:    */   public static boolean isComponentRecord(int sid)
/*  21:    */   {
/*  22: 58 */     switch (sid)
/*  23:    */     {
/*  24:    */     case 18: 
/*  25:    */     case 19: 
/*  26:    */     case 99: 
/*  27:    */     case 221: 
/*  28: 63 */       return true;
/*  29:    */     }
/*  30: 65 */     return false;
/*  31:    */   }
/*  32:    */   
/*  33:    */   private boolean readARecord(RecordStream rs)
/*  34:    */   {
/*  35: 69 */     switch (rs.peekNextSid())
/*  36:    */     {
/*  37:    */     case 18: 
/*  38: 71 */       checkNotPresent(this._protectRecord);
/*  39: 72 */       this._protectRecord = ((ProtectRecord)rs.getNext());
/*  40: 73 */       break;
/*  41:    */     case 99: 
/*  42: 75 */       checkNotPresent(this._objectProtectRecord);
/*  43: 76 */       this._objectProtectRecord = ((ObjectProtectRecord)rs.getNext());
/*  44: 77 */       break;
/*  45:    */     case 221: 
/*  46: 79 */       checkNotPresent(this._scenarioProtectRecord);
/*  47: 80 */       this._scenarioProtectRecord = ((ScenarioProtectRecord)rs.getNext());
/*  48: 81 */       break;
/*  49:    */     case 19: 
/*  50: 83 */       checkNotPresent(this._passwordRecord);
/*  51: 84 */       this._passwordRecord = ((PasswordRecord)rs.getNext());
/*  52: 85 */       break;
/*  53:    */     default: 
/*  54: 88 */       return false;
/*  55:    */     }
/*  56: 90 */     return true;
/*  57:    */   }
/*  58:    */   
/*  59:    */   private void checkNotPresent(Record rec)
/*  60:    */   {
/*  61: 94 */     if (rec != null) {
/*  62: 95 */       throw new RecordFormatException("Duplicate PageSettingsBlock record (sid=0x" + Integer.toHexString(rec.getSid()) + ")");
/*  63:    */     }
/*  64:    */   }
/*  65:    */   
/*  66:    */   public void visitContainedRecords(RecordVisitor rv)
/*  67:    */   {
/*  68:103 */     visitIfPresent(this._protectRecord, rv);
/*  69:104 */     visitIfPresent(this._objectProtectRecord, rv);
/*  70:105 */     visitIfPresent(this._scenarioProtectRecord, rv);
/*  71:106 */     visitIfPresent(this._passwordRecord, rv);
/*  72:    */   }
/*  73:    */   
/*  74:    */   private static void visitIfPresent(Record r, RecordVisitor rv)
/*  75:    */   {
/*  76:110 */     if (r != null) {
/*  77:111 */       rv.visitRecord(r);
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public PasswordRecord getPasswordRecord()
/*  82:    */   {
/*  83:116 */     return this._passwordRecord;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public ScenarioProtectRecord getHCenter()
/*  87:    */   {
/*  88:120 */     return this._scenarioProtectRecord;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void addRecords(RecordStream rs)
/*  92:    */   {
/*  93:    */     for (;;)
/*  94:    */     {
/*  95:141 */       if (!readARecord(rs)) {
/*  96:    */         break;
/*  97:    */       }
/*  98:    */     }
/*  99:    */   }
/* 100:    */   
/* 101:    */   private ProtectRecord getProtect()
/* 102:    */   {
/* 103:152 */     if (this._protectRecord == null) {
/* 104:153 */       this._protectRecord = new ProtectRecord(false);
/* 105:    */     }
/* 106:155 */     return this._protectRecord;
/* 107:    */   }
/* 108:    */   
/* 109:    */   private PasswordRecord getPassword()
/* 110:    */   {
/* 111:163 */     if (this._passwordRecord == null) {
/* 112:164 */       this._passwordRecord = createPassword();
/* 113:    */     }
/* 114:166 */     return this._passwordRecord;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public void protectSheet(String password, boolean shouldProtectObjects, boolean shouldProtectScenarios)
/* 118:    */   {
/* 119:179 */     if (password == null)
/* 120:    */     {
/* 121:180 */       this._passwordRecord = null;
/* 122:181 */       this._protectRecord = null;
/* 123:182 */       this._objectProtectRecord = null;
/* 124:183 */       this._scenarioProtectRecord = null;
/* 125:184 */       return;
/* 126:    */     }
/* 127:187 */     ProtectRecord prec = getProtect();
/* 128:188 */     PasswordRecord pass = getPassword();
/* 129:189 */     prec.setProtect(true);
/* 130:190 */     pass.setPassword((short)CryptoFunctions.createXorVerifier1(password));
/* 131:191 */     if ((this._objectProtectRecord == null) && (shouldProtectObjects))
/* 132:    */     {
/* 133:192 */       ObjectProtectRecord rec = createObjectProtect();
/* 134:193 */       rec.setProtect(true);
/* 135:194 */       this._objectProtectRecord = rec;
/* 136:    */     }
/* 137:196 */     if ((this._scenarioProtectRecord == null) && (shouldProtectScenarios))
/* 138:    */     {
/* 139:197 */       ScenarioProtectRecord srec = createScenarioProtect();
/* 140:198 */       srec.setProtect(true);
/* 141:199 */       this._scenarioProtectRecord = srec;
/* 142:    */     }
/* 143:    */   }
/* 144:    */   
/* 145:    */   public boolean isSheetProtected()
/* 146:    */   {
/* 147:204 */     return (this._protectRecord != null) && (this._protectRecord.getProtect());
/* 148:    */   }
/* 149:    */   
/* 150:    */   public boolean isObjectProtected()
/* 151:    */   {
/* 152:208 */     return (this._objectProtectRecord != null) && (this._objectProtectRecord.getProtect());
/* 153:    */   }
/* 154:    */   
/* 155:    */   public boolean isScenarioProtected()
/* 156:    */   {
/* 157:212 */     return (this._scenarioProtectRecord != null) && (this._scenarioProtectRecord.getProtect());
/* 158:    */   }
/* 159:    */   
/* 160:    */   private static ObjectProtectRecord createObjectProtect()
/* 161:    */   {
/* 162:219 */     ObjectProtectRecord retval = new ObjectProtectRecord();
/* 163:220 */     retval.setProtect(false);
/* 164:221 */     return retval;
/* 165:    */   }
/* 166:    */   
/* 167:    */   private static ScenarioProtectRecord createScenarioProtect()
/* 168:    */   {
/* 169:228 */     ScenarioProtectRecord retval = new ScenarioProtectRecord();
/* 170:229 */     retval.setProtect(false);
/* 171:230 */     return retval;
/* 172:    */   }
/* 173:    */   
/* 174:    */   private static PasswordRecord createPassword()
/* 175:    */   {
/* 176:237 */     return new PasswordRecord(0);
/* 177:    */   }
/* 178:    */   
/* 179:    */   public int getPasswordHash()
/* 180:    */   {
/* 181:241 */     if (this._passwordRecord == null) {
/* 182:242 */       return 0;
/* 183:    */     }
/* 184:244 */     return this._passwordRecord.getPassword();
/* 185:    */   }
/* 186:    */ }



/* Location:           F:\poi-3.17.jar

 * Qualified Name:     org.apache.poi.hssf.record.aggregates.WorksheetProtectionBlock

 * JD-Core Version:    0.7.0.1

 */