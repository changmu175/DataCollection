/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.io.FilterInputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.nio.charset.Charset;
/*   7:    */ import java.util.Arrays;
/*   8:    */ 
/*   9:    */ @Internal
/*  10:    */ public class ReplacingInputStream
/*  11:    */   extends FilterInputStream
/*  12:    */ {
/*  13:    */   final int[] buf;
/*  14: 38 */   private int matchedIndex = 0;
/*  15: 39 */   private int unbufferIndex = 0;
/*  16: 40 */   private int replacedIndex = 0;
/*  17:    */   private final byte[] pattern;
/*  18:    */   private final byte[] replacement;
/*  19: 44 */   private State state = State.NOT_MATCHED;
/*  20:    */   
/*  21:    */   private static enum State
/*  22:    */   {
/*  23: 48 */     NOT_MATCHED,  MATCHING,  REPLACING,  UNBUFFER;
/*  24:    */     
/*  25:    */     private State() {}
/*  26:    */   }
/*  27:    */   
/*  28: 54 */   private static final Charset UTF8 = Charset.forName("UTF-8");
/*  29:    */   
/*  30:    */   public ReplacingInputStream(InputStream in, String pattern, String replacement)
/*  31:    */   {
/*  32: 63 */     this(in, pattern.getBytes(UTF8), replacement == null ? null : replacement.getBytes(UTF8));
/*  33:    */   }
/*  34:    */   
/*  35:    */   public ReplacingInputStream(InputStream in, byte[] pattern, byte[] replacement)
/*  36:    */   {
/*  37: 77 */     super(in);
/*  38: 78 */     if ((pattern == null) || (pattern.length == 0)) {
/*  39: 79 */       throw new IllegalArgumentException("pattern length should be > 0");
/*  40:    */     }
/*  41: 81 */     this.pattern = pattern;
/*  42: 82 */     this.replacement = replacement;
/*  43:    */     
/*  44: 84 */     this.buf = new int[pattern.length];
/*  45:    */   }
/*  46:    */   
/*  47:    */   public int read(byte[] b, int off, int len)
/*  48:    */     throws IOException
/*  49:    */   {
/*  50: 90 */     if (b == null) {
/*  51: 91 */       throw new NullPointerException();
/*  52:    */     }
/*  53: 92 */     if ((off < 0) || (len < 0) || (len > b.length - off)) {
/*  54: 93 */       throw new IndexOutOfBoundsException();
/*  55:    */     }
/*  56: 94 */     if (len == 0) {
/*  57: 95 */       return 0;
/*  58:    */     }
/*  59: 98 */     int c = read();
/*  60: 99 */     if (c == -1) {
/*  61:100 */       return -1;
/*  62:    */     }
/*  63:102 */     b[off] = ((byte)c);
/*  64:104 */     for (int i = 1; i < len; i++)
/*  65:    */     {
/*  66:106 */       c = read();
/*  67:107 */       if (c == -1) {
/*  68:    */         break;
/*  69:    */       }
/*  70:110 */       b[(off + i)] = ((byte)c);
/*  71:    */     }
/*  72:112 */     return i;
/*  73:    */   }
/*  74:    */   
/*  75:    */   public int read(byte[] b)
/*  76:    */     throws IOException
/*  77:    */   {
/*  78:119 */     return read(b, 0, b.length);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public int read()
/*  82:    */     throws IOException
/*  83:    */   {
/*  84:126 */     switch (1.$SwitchMap$org$apache$poi$util$ReplacingInputStream$State[this.state.ordinal()])
/*  85:    */     {
/*  86:    */     case 1: 
/*  87:    */     default: 
/*  88:130 */       next = super.read();
/*  89:131 */       if (this.pattern[0] != next) {
/*  90:132 */         return next;
/*  91:    */       }
/*  92:136 */       Arrays.fill(this.buf, 0);
/*  93:    */       
/*  94:138 */       this.matchedIndex = 0;
/*  95:    */       
/*  96:140 */       this.buf[(this.matchedIndex++)] = next;
/*  97:141 */       if (this.pattern.length == 1)
/*  98:    */       {
/*  99:143 */         this.state = State.REPLACING;
/* 100:    */         
/* 101:145 */         this.replacedIndex = 0;
/* 102:    */       }
/* 103:    */       else
/* 104:    */       {
/* 105:148 */         this.state = State.MATCHING;
/* 106:    */       }
/* 107:151 */       return read();
/* 108:    */     case 2: 
/* 109:155 */       next = super.read();
/* 110:156 */       if (this.pattern[this.matchedIndex] == next)
/* 111:    */       {
/* 112:157 */         this.buf[(this.matchedIndex++)] = next;
/* 113:158 */         if (this.matchedIndex == this.pattern.length) {
/* 114:160 */           if ((this.replacement == null) || (this.replacement.length == 0))
/* 115:    */           {
/* 116:162 */             this.state = State.NOT_MATCHED;
/* 117:163 */             this.matchedIndex = 0;
/* 118:    */           }
/* 119:    */           else
/* 120:    */           {
/* 121:166 */             this.state = State.REPLACING;
/* 122:167 */             this.replacedIndex = 0;
/* 123:    */           }
/* 124:    */         }
/* 125:    */       }
/* 126:    */       else
/* 127:    */       {
/* 128:172 */         this.buf[(this.matchedIndex++)] = next;
/* 129:173 */         this.state = State.UNBUFFER;
/* 130:174 */         this.unbufferIndex = 0;
/* 131:    */       }
/* 132:176 */       return read();
/* 133:    */     case 3: 
/* 134:180 */       next = this.replacement[(this.replacedIndex++)];
/* 135:181 */       if (this.replacedIndex == this.replacement.length)
/* 136:    */       {
/* 137:182 */         this.state = State.NOT_MATCHED;
/* 138:183 */         this.replacedIndex = 0;
/* 139:    */       }
/* 140:185 */       return next;
/* 141:    */     }
/* 142:190 */     int next = this.buf[(this.unbufferIndex++)];
/* 143:191 */     if (this.unbufferIndex == this.matchedIndex)
/* 144:    */     {
/* 145:192 */       this.state = State.NOT_MATCHED;
/* 146:193 */       this.matchedIndex = 0;
/* 147:    */     }
/* 148:195 */     return next;
/* 149:    */   }
/* 150:    */   
/* 151:    */   public String toString()
/* 152:    */   {
/* 153:201 */     return this.state.name() + " " + this.matchedIndex + " " + this.replacedIndex + " " + this.unbufferIndex;
/* 154:    */   }
/* 155:    */ }


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.util.ReplacingInputStream
 * JD-Core Version:    0.7.0.1
 */