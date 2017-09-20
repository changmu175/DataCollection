/*   1:    */ package org.apache.poi.hpsf;
/*   2:    */ 
/*   3:    */ public class CustomProperty
/*   4:    */   extends MutableProperty
/*   5:    */ {
/*   6:    */   private String name;
/*   7:    */   
/*   8:    */   public CustomProperty()
/*   9:    */   {
/*  10: 36 */     this.name = null;
/*  11:    */   }
/*  12:    */   
/*  13:    */   public CustomProperty(Property property)
/*  14:    */   {
/*  15: 46 */     this(property, null);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public CustomProperty(Property property, String name)
/*  19:    */   {
/*  20: 57 */     super(property);
/*  21: 58 */     this.name = name;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public String getName()
/*  25:    */   {
/*  26: 67 */     return this.name;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setName(String name)
/*  30:    */   {
/*  31: 76 */     this.name = name;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public boolean equalsContents(Object o)
/*  35:    */   {
/*  36: 92 */     CustomProperty c = (CustomProperty)o;
/*  37: 93 */     String name1 = c.getName();
/*  38: 94 */     String name2 = getName();
/*  39: 95 */     boolean equalNames = true;
/*  40: 96 */     if (name1 == null) {
/*  41: 97 */       equalNames = name2 == null;
/*  42:    */     } else {
/*  43: 99 */       equalNames = name1.equals(name2);
/*  44:    */     }
/*  45:101 */     return (equalNames) && (c.getID() == getID()) && (c.getType() == getType()) && (c.getValue().equals(getValue()));
/*  46:    */   }
/*  47:    */   
/*  48:    */   public int hashCode()
/*  49:    */   {
/*  50:111 */     return (int)getID();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public boolean equals(Object o)
/*  54:    */   {
/*  55:116 */     return (o instanceof CustomProperty) ? equalsContents(o) : false;
/*  56:    */   }
/*  57:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.hpsf.CustomProperty
 * JD-Core Version:    0.7.0.1
 */