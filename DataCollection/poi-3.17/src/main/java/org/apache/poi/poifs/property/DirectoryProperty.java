/*   1:    */ package org.apache.poi.poifs.property;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.Serializable;
/*   5:    */ import java.util.ArrayList;
/*   6:    */ import java.util.Arrays;
/*   7:    */ import java.util.Comparator;
/*   8:    */ import java.util.HashSet;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Set;
/*  12:    */ 
/*  13:    */ public class DirectoryProperty
/*  14:    */   extends Property
/*  15:    */   implements Parent, Iterable<Property>
/*  16:    */ {
/*  17:    */   private List<Property> _children;
/*  18:    */   private Set<String> _children_names;
/*  19:    */   
/*  20:    */   public DirectoryProperty(String name)
/*  21:    */   {
/*  22: 49 */     this._children = new ArrayList();
/*  23: 50 */     this._children_names = new HashSet();
/*  24: 51 */     setName(name);
/*  25: 52 */     setSize(0);
/*  26: 53 */     setPropertyType((byte)1);
/*  27: 54 */     setStartBlock(0);
/*  28: 55 */     setNodeColor((byte)1);
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected DirectoryProperty(int index, byte[] array, int offset)
/*  32:    */   {
/*  33: 68 */     super(index, array, offset);
/*  34: 69 */     this._children = new ArrayList();
/*  35: 70 */     this._children_names = new HashSet();
/*  36:    */   }
/*  37:    */   
/*  38:    */   public boolean changeName(Property property, String newName)
/*  39:    */   {
/*  40: 84 */     String oldName = property.getName();
/*  41:    */     
/*  42: 86 */     property.setName(newName);
/*  43: 87 */     String cleanNewName = property.getName();
/*  44:    */     boolean result;
/*  45:    */     boolean result;
/*  46: 89 */     if (this._children_names.contains(cleanNewName))
/*  47:    */     {
/*  48: 93 */       property.setName(oldName);
/*  49: 94 */       result = false;
/*  50:    */     }
/*  51:    */     else
/*  52:    */     {
/*  53: 98 */       this._children_names.add(cleanNewName);
/*  54: 99 */       this._children_names.remove(oldName);
/*  55:100 */       result = true;
/*  56:    */     }
/*  57:102 */     return result;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public boolean deleteChild(Property property)
/*  61:    */   {
/*  62:114 */     boolean result = this._children.remove(property);
/*  63:116 */     if (result) {
/*  64:118 */       this._children_names.remove(property.getName());
/*  65:    */     }
/*  66:120 */     return result;
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static class PropertyComparator
/*  70:    */     implements Comparator<Property>, Serializable
/*  71:    */   {
/*  72:    */     public int compare(Property o1, Property o2)
/*  73:    */     {
/*  74:142 */       String VBA_PROJECT = "_VBA_PROJECT";
/*  75:143 */       String name1 = o1.getName();
/*  76:144 */       String name2 = o2.getName();
/*  77:145 */       int result = name1.length() - name2.length();
/*  78:147 */       if (result == 0) {
/*  79:150 */         if (name1.compareTo(VBA_PROJECT) == 0) {
/*  80:151 */           result = 1;
/*  81:152 */         } else if (name2.compareTo(VBA_PROJECT) == 0) {
/*  82:153 */           result = -1;
/*  83:156 */         } else if ((name1.startsWith("__")) && (name2.startsWith("__"))) {
/*  84:159 */           result = name1.compareToIgnoreCase(name2);
/*  85:161 */         } else if (name1.startsWith("__")) {
/*  86:164 */           result = 1;
/*  87:166 */         } else if (name2.startsWith("__")) {
/*  88:169 */           result = -1;
/*  89:    */         } else {
/*  90:174 */           result = name1.compareToIgnoreCase(name2);
/*  91:    */         }
/*  92:    */       }
/*  93:177 */       return result;
/*  94:    */     }
/*  95:    */   }
/*  96:    */   
/*  97:    */   public boolean isDirectory()
/*  98:    */   {
/*  99:186 */     return true;
/* 100:    */   }
/* 101:    */   
/* 102:    */   protected void preWrite()
/* 103:    */   {
/* 104:195 */     if (this._children.size() > 0)
/* 105:    */     {
/* 106:197 */       Property[] children = (Property[])this._children.toArray(new Property[0]);
/* 107:    */       
/* 108:199 */       Arrays.sort(children, new PropertyComparator());
/* 109:200 */       int midpoint = children.length / 2;
/* 110:    */       
/* 111:202 */       setChildProperty(children[midpoint].getIndex());
/* 112:203 */       children[0].setPreviousChild(null);
/* 113:204 */       children[0].setNextChild(null);
/* 114:205 */       for (int j = 1; j < midpoint; j++)
/* 115:    */       {
/* 116:207 */         children[j].setPreviousChild(children[(j - 1)]);
/* 117:208 */         children[j].setNextChild(null);
/* 118:    */       }
/* 119:210 */       if (midpoint != 0) {
/* 120:212 */         children[midpoint].setPreviousChild(children[(midpoint - 1)]);
/* 121:    */       }
/* 122:215 */       if (midpoint != children.length - 1)
/* 123:    */       {
/* 124:217 */         children[midpoint].setNextChild(children[(midpoint + 1)]);
/* 125:218 */         for (int j = midpoint + 1; j < children.length - 1; j++)
/* 126:    */         {
/* 127:220 */           children[j].setPreviousChild(null);
/* 128:221 */           children[j].setNextChild(children[(j + 1)]);
/* 129:    */         }
/* 130:223 */         children[(children.length - 1)].setPreviousChild(null);
/* 131:224 */         children[(children.length - 1)].setNextChild(null);
/* 132:    */       }
/* 133:    */       else
/* 134:    */       {
/* 135:228 */         children[midpoint].setNextChild(null);
/* 136:    */       }
/* 137:    */     }
/* 138:    */   }
/* 139:    */   
/* 140:    */   public Iterator<Property> getChildren()
/* 141:    */   {
/* 142:241 */     return this._children.iterator();
/* 143:    */   }
/* 144:    */   
/* 145:    */   public Iterator<Property> iterator()
/* 146:    */   {
/* 147:248 */     return getChildren();
/* 148:    */   }
/* 149:    */   
/* 150:    */   public void addChild(Property property)
/* 151:    */     throws IOException
/* 152:    */   {
/* 153:262 */     String name = property.getName();
/* 154:264 */     if (this._children_names.contains(name)) {
/* 155:266 */       throw new IOException("Duplicate name \"" + name + "\"");
/* 156:    */     }
/* 157:268 */     this._children_names.add(name);
/* 158:269 */     this._children.add(property);
/* 159:    */   }
/* 160:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.property.DirectoryProperty
 * JD-Core Version:    0.7.0.1
 */