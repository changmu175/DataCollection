/*   1:    */ package org.apache.poi.hssf.record;
/*   2:    */ 
/*   3:    */ import org.apache.poi.util.LittleEndianOutput;
/*   4:    */ import org.apache.poi.util.StringUtil;
/*   5:    */ 
/*   6:    */ public abstract class HeaderFooterBase
/*   7:    */   extends StandardRecord
/*   8:    */ {
/*   9:    */   private boolean field_2_hasMultibyte;
/*  10:    */   private String field_3_text;
/*  11:    */   
/*  12:    */   protected HeaderFooterBase(String text)
/*  13:    */   {
/*  14: 33 */     setText(text);
/*  15:    */   }
/*  16:    */   
/*  17:    */   protected HeaderFooterBase(RecordInputStream in)
/*  18:    */   {
/*  19: 37 */     if (in.remaining() > 0)
/*  20:    */     {
/*  21: 38 */       int field_1_footer_len = in.readShort();
/*  22: 40 */       if (field_1_footer_len == 0)
/*  23:    */       {
/*  24: 41 */         this.field_3_text = "";
/*  25: 42 */         if (in.remaining() == 0) {
/*  26: 43 */           return;
/*  27:    */         }
/*  28:    */       }
/*  29: 46 */       this.field_2_hasMultibyte = (in.readByte() != 0);
/*  30: 48 */       if (this.field_2_hasMultibyte) {
/*  31: 49 */         this.field_3_text = in.readUnicodeLEString(field_1_footer_len);
/*  32:    */       } else {
/*  33: 51 */         this.field_3_text = in.readCompressedUnicode(field_1_footer_len);
/*  34:    */       }
/*  35:    */     }
/*  36:    */     else
/*  37:    */     {
/*  38: 56 */       this.field_3_text = "";
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public final void setText(String text)
/*  43:    */   {
/*  44: 66 */     if (text == null) {
/*  45: 67 */       throw new IllegalArgumentException("text must not be null");
/*  46:    */     }
/*  47: 69 */     this.field_2_hasMultibyte = StringUtil.hasMultibyte(text);
/*  48: 70 */     this.field_3_text = text;
/*  49: 73 */     if (getDataSize() > 8224) {
/*  50: 74 */       throw new IllegalArgumentException("Header/Footer string too long (limit is 8224 bytes)");
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   private int getTextLength()
/*  55:    */   {
/*  56: 85 */     return this.field_3_text.length();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public final String getText()
/*  60:    */   {
/*  61: 89 */     return this.field_3_text;
/*  62:    */   }
/*  63:    */   
/*  64:    */   public final void serialize(LittleEndianOutput out)
/*  65:    */   {
/*  66: 93 */     if (getTextLength() > 0)
/*  67:    */     {
/*  68: 94 */       out.writeShort(getTextLength());
/*  69: 95 */       out.writeByte(this.field_2_hasMultibyte ? 1 : 0);
/*  70: 96 */       if (this.field_2_hasMultibyte) {
/*  71: 97 */         StringUtil.putUnicodeLE(this.field_3_text, out);
/*  72:    */       } else {
/*  73: 99 */         StringUtil.putCompressedUnicode(this.field_3_text, out);
/*  74:    */       }
/*  75:    */     }
/*  76:    */   }
/*  77:    */   
/*  78:    */   protected final int getDataSize()
/*  79:    */   {
/*  80:105 */     if (getTextLength() < 1) {
/*  81:106 */       return 0;
/*  82:    */     }
/*  83:108 */     return 3 + getTextLength() * (this.field_2_hasMultibyte ? 2 : 1);
/*  84:    */   }
/*  85:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.HeaderFooterBase
 * JD-Core Version:    0.7.0.1
 */