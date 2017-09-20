/*   1:    */ package org.apache.poi.poifs.property;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import java.util.Stack;
/*   7:    */ import org.apache.poi.poifs.filesystem.BATManaged;
/*   8:    */ import org.apache.poi.poifs.storage.HeaderBlock;
/*   9:    */ import org.apache.poi.util.POILogFactory;
/*  10:    */ import org.apache.poi.util.POILogger;
/*  11:    */ 
/*  12:    */ public abstract class PropertyTableBase
/*  13:    */   implements BATManaged
/*  14:    */ {
/*  15: 38 */   private static final POILogger _logger = POILogFactory.getLogger(PropertyTableBase.class);
/*  16:    */   private final HeaderBlock _header_block;
/*  17:    */   protected final List<Property> _properties;
/*  18:    */   
/*  19:    */   public PropertyTableBase(HeaderBlock header_block)
/*  20:    */   {
/*  21: 46 */     this._header_block = header_block;
/*  22: 47 */     this._properties = new ArrayList();
/*  23: 48 */     addProperty(new RootProperty());
/*  24:    */   }
/*  25:    */   
/*  26:    */   public PropertyTableBase(HeaderBlock header_block, List<Property> properties)
/*  27:    */     throws IOException
/*  28:    */   {
/*  29: 66 */     this._header_block = header_block;
/*  30: 67 */     this._properties = properties;
/*  31: 68 */     populatePropertyTree((DirectoryProperty)this._properties.get(0));
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void addProperty(Property property)
/*  35:    */   {
/*  36: 78 */     this._properties.add(property);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void removeProperty(Property property)
/*  40:    */   {
/*  41: 88 */     this._properties.remove(property);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public RootProperty getRoot()
/*  45:    */   {
/*  46: 99 */     return (RootProperty)this._properties.get(0);
/*  47:    */   }
/*  48:    */   
/*  49:    */   private void populatePropertyTree(DirectoryProperty root)
/*  50:    */     throws IOException
/*  51:    */   {
/*  52:105 */     int index = root.getChildIndex();
/*  53:107 */     if (!Property.isValidIndex(index)) {
/*  54:111 */       return;
/*  55:    */     }
/*  56:113 */     Stack<Property> children = new Stack();
/*  57:    */     
/*  58:115 */     children.push(this._properties.get(index));
/*  59:116 */     while (!children.empty())
/*  60:    */     {
/*  61:118 */       Property property = (Property)children.pop();
/*  62:119 */       if (property != null)
/*  63:    */       {
/*  64:125 */         root.addChild(property);
/*  65:126 */         if (property.isDirectory()) {
/*  66:128 */           populatePropertyTree((DirectoryProperty)property);
/*  67:    */         }
/*  68:130 */         index = property.getPreviousChildIndex();
/*  69:131 */         if (isValidIndex(index)) {
/*  70:133 */           children.push(this._properties.get(index));
/*  71:    */         }
/*  72:135 */         index = property.getNextChildIndex();
/*  73:136 */         if (isValidIndex(index)) {
/*  74:138 */           children.push(this._properties.get(index));
/*  75:    */         }
/*  76:    */       }
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected boolean isValidIndex(int index)
/*  81:    */   {
/*  82:144 */     if (!Property.isValidIndex(index)) {
/*  83:145 */       return false;
/*  84:    */     }
/*  85:146 */     if ((index < 0) || (index >= this._properties.size()))
/*  86:    */     {
/*  87:147 */       _logger.log(5, new Object[] { "Property index " + index + "outside the valid range 0.." + this._properties.size() });
/*  88:    */       
/*  89:149 */       return false;
/*  90:    */     }
/*  91:151 */     return true;
/*  92:    */   }
/*  93:    */   
/*  94:    */   public int getStartBlock()
/*  95:    */   {
/*  96:161 */     return this._header_block.getPropertyStart();
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void setStartBlock(int index)
/* 100:    */   {
/* 101:172 */     this._header_block.setPropertyStart(index);
/* 102:    */   }
/* 103:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.poifs.property.PropertyTableBase
 * JD-Core Version:    0.7.0.1
 */