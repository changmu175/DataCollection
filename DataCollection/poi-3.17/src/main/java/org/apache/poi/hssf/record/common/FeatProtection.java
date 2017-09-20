/*   1:    */ package org.apache.poi.hssf.record.common;
/*   2:    */ 
/*   3:    */ import org.apache.poi.hssf.record.RecordInputStream;
/*   4:    */ import org.apache.poi.util.LittleEndianOutput;
/*   5:    */ import org.apache.poi.util.StringUtil;
/*   6:    */ 
/*   7:    */ public final class FeatProtection
/*   8:    */   implements SharedFeature
/*   9:    */ {
/*  10: 37 */   public static long NO_SELF_RELATIVE_SECURITY_FEATURE = 0L;
/*  11: 38 */   public static long HAS_SELF_RELATIVE_SECURITY_FEATURE = 1L;
/*  12:    */   private int fSD;
/*  13:    */   private int passwordVerifier;
/*  14:    */   private String title;
/*  15:    */   private byte[] securityDescriptor;
/*  16:    */   
/*  17:    */   public FeatProtection()
/*  18:    */   {
/*  19: 54 */     this.securityDescriptor = new byte[0];
/*  20:    */   }
/*  21:    */   
/*  22:    */   public FeatProtection(RecordInputStream in)
/*  23:    */   {
/*  24: 58 */     this.fSD = in.readInt();
/*  25: 59 */     this.passwordVerifier = in.readInt();
/*  26:    */     
/*  27: 61 */     this.title = StringUtil.readUnicodeString(in);
/*  28:    */     
/*  29: 63 */     this.securityDescriptor = in.readRemainder();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public String toString()
/*  33:    */   {
/*  34: 67 */     StringBuffer buffer = new StringBuffer();
/*  35: 68 */     buffer.append(" [FEATURE PROTECTION]\n");
/*  36: 69 */     buffer.append("   Self Relative = " + this.fSD);
/*  37: 70 */     buffer.append("   Password Verifier = " + this.passwordVerifier);
/*  38: 71 */     buffer.append("   Title = " + this.title);
/*  39: 72 */     buffer.append("   Security Descriptor Size = " + this.securityDescriptor.length);
/*  40: 73 */     buffer.append(" [/FEATURE PROTECTION]\n");
/*  41: 74 */     return buffer.toString();
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void serialize(LittleEndianOutput out)
/*  45:    */   {
/*  46: 78 */     out.writeInt(this.fSD);
/*  47: 79 */     out.writeInt(this.passwordVerifier);
/*  48: 80 */     StringUtil.writeUnicodeString(out, this.title);
/*  49: 81 */     out.write(this.securityDescriptor);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public int getDataSize()
/*  53:    */   {
/*  54: 85 */     return 8 + StringUtil.getEncodedSize(this.title) + this.securityDescriptor.length;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public int getPasswordVerifier()
/*  58:    */   {
/*  59: 89 */     return this.passwordVerifier;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public void setPasswordVerifier(int passwordVerifier)
/*  63:    */   {
/*  64: 92 */     this.passwordVerifier = passwordVerifier;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public String getTitle()
/*  68:    */   {
/*  69: 96 */     return this.title;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setTitle(String title)
/*  73:    */   {
/*  74: 99 */     this.title = title;
/*  75:    */   }
/*  76:    */   
/*  77:    */   public int getFSD()
/*  78:    */   {
/*  79:103 */     return this.fSD;
/*  80:    */   }
/*  81:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hssf.record.common.FeatProtection
 * JD-Core Version:    0.7.0.1
 */