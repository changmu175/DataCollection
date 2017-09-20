/*   1:    */ package org.apache.poi.util;
/*   2:    */ 
/*   3:    */ import java.util.LinkedList;
/*   4:    */ import java.util.ListIterator;
/*   5:    */ 
/*   6:    */ public class IdentifierManager
/*   7:    */ {
/*   8:    */   public static final long MAX_ID = 9223372036854775806L;
/*   9:    */   public static final long MIN_ID = 0L;
/*  10:    */   private final long upperbound;
/*  11:    */   private final long lowerbound;
/*  12:    */   private LinkedList<Segment> segments;
/*  13:    */   
/*  14:    */   public IdentifierManager(long lowerbound, long upperbound)
/*  15:    */   {
/*  16: 56 */     if (lowerbound > upperbound) {
/*  17: 57 */       throw new IllegalArgumentException("lowerbound must not be greater than upperbound, had " + lowerbound + " and " + upperbound);
/*  18:    */     }
/*  19: 59 */     if (lowerbound < 0L)
/*  20:    */     {
/*  21: 60 */       String message = "lowerbound must be greater than or equal to " + Long.toString(0L);
/*  22: 61 */       throw new IllegalArgumentException(message);
/*  23:    */     }
/*  24: 63 */     if (upperbound > 9223372036854775806L) {
/*  25: 68 */       throw new IllegalArgumentException("upperbound must be less than or equal to " + Long.toString(9223372036854775806L) + " but had " + upperbound);
/*  26:    */     }
/*  27: 70 */     this.lowerbound = lowerbound;
/*  28: 71 */     this.upperbound = upperbound;
/*  29: 72 */     this.segments = new LinkedList();
/*  30: 73 */     this.segments.add(new Segment(lowerbound, upperbound));
/*  31:    */   }
/*  32:    */   
/*  33:    */   public long reserve(long id)
/*  34:    */   {
/*  35: 77 */     if ((id < this.lowerbound) || (id > this.upperbound)) {
/*  36: 78 */       throw new IllegalArgumentException("Value for parameter 'id' was out of bounds, had " + id + ", but should be within [" + this.lowerbound + ":" + this.upperbound + "]");
/*  37:    */     }
/*  38: 80 */     verifyIdentifiersLeft();
/*  39: 82 */     if (id == this.upperbound)
/*  40:    */     {
/*  41: 83 */       Segment lastSegment = (Segment)this.segments.getLast();
/*  42: 84 */       if (lastSegment.end == this.upperbound)
/*  43:    */       {
/*  44: 85 */         lastSegment.end = (this.upperbound - 1L);
/*  45: 86 */         if (lastSegment.start > lastSegment.end) {
/*  46: 87 */           this.segments.removeLast();
/*  47:    */         }
/*  48: 89 */         return id;
/*  49:    */       }
/*  50: 91 */       return reserveNew();
/*  51:    */     }
/*  52: 94 */     if (id == this.lowerbound)
/*  53:    */     {
/*  54: 95 */       Segment firstSegment = (Segment)this.segments.getFirst();
/*  55: 96 */       if (firstSegment.start == this.lowerbound)
/*  56:    */       {
/*  57: 97 */         firstSegment.start = (this.lowerbound + 1L);
/*  58: 98 */         if (firstSegment.end < firstSegment.start) {
/*  59: 99 */           this.segments.removeFirst();
/*  60:    */         }
/*  61:101 */         return id;
/*  62:    */       }
/*  63:103 */       return reserveNew();
/*  64:    */     }
/*  65:106 */     ListIterator<Segment> iter = this.segments.listIterator();
/*  66:107 */     while (iter.hasNext())
/*  67:    */     {
/*  68:108 */       Segment segment = (Segment)iter.next();
/*  69:109 */       if (segment.end >= id) {
/*  70:112 */         if (segment.start <= id)
/*  71:    */         {
/*  72:115 */           if (segment.start == id)
/*  73:    */           {
/*  74:116 */             segment.start = (id + 1L);
/*  75:117 */             if (segment.end < segment.start) {
/*  76:118 */               iter.remove();
/*  77:    */             }
/*  78:120 */             return id;
/*  79:    */           }
/*  80:122 */           if (segment.end == id)
/*  81:    */           {
/*  82:123 */             segment.end = (id - 1L);
/*  83:124 */             if (segment.start > segment.end) {
/*  84:125 */               iter.remove();
/*  85:    */             }
/*  86:127 */             return id;
/*  87:    */           }
/*  88:130 */           iter.add(new Segment(id + 1L, segment.end));
/*  89:131 */           segment.end = (id - 1L);
/*  90:132 */           return id;
/*  91:    */         }
/*  92:    */       }
/*  93:    */     }
/*  94:135 */     return reserveNew();
/*  95:    */   }
/*  96:    */   
/*  97:    */   public long reserveNew()
/*  98:    */   {
/*  99:143 */     verifyIdentifiersLeft();
/* 100:144 */     Segment segment = (Segment)this.segments.getFirst();
/* 101:145 */     long result = segment.start;
/* 102:146 */     segment.start += 1L;
/* 103:147 */     if (segment.start > segment.end) {
/* 104:148 */       this.segments.removeFirst();
/* 105:    */     }
/* 106:150 */     return result;
/* 107:    */   }
/* 108:    */   
/* 109:    */   public boolean release(long id)
/* 110:    */   {
/* 111:161 */     if ((id < this.lowerbound) || (id > this.upperbound)) {
/* 112:162 */       throw new IllegalArgumentException("Value for parameter 'id' was out of bounds, had " + id + ", but should be within [" + this.lowerbound + ":" + this.upperbound + "]");
/* 113:    */     }
/* 114:165 */     if (id == this.upperbound)
/* 115:    */     {
/* 116:166 */       Segment lastSegment = (Segment)this.segments.getLast();
/* 117:167 */       if (lastSegment.end == this.upperbound - 1L)
/* 118:    */       {
/* 119:168 */         lastSegment.end = this.upperbound;
/* 120:169 */         return true;
/* 121:    */       }
/* 122:170 */       if (lastSegment.end == this.upperbound) {
/* 123:171 */         return false;
/* 124:    */       }
/* 125:173 */       this.segments.add(new Segment(this.upperbound, this.upperbound));
/* 126:174 */       return true;
/* 127:    */     }
/* 128:178 */     if (id == this.lowerbound)
/* 129:    */     {
/* 130:179 */       Segment firstSegment = (Segment)this.segments.getFirst();
/* 131:180 */       if (firstSegment.start == this.lowerbound + 1L)
/* 132:    */       {
/* 133:181 */         firstSegment.start = this.lowerbound;
/* 134:182 */         return true;
/* 135:    */       }
/* 136:183 */       if (firstSegment.start == this.lowerbound) {
/* 137:184 */         return false;
/* 138:    */       }
/* 139:186 */       this.segments.addFirst(new Segment(this.lowerbound, this.lowerbound));
/* 140:187 */       return true;
/* 141:    */     }
/* 142:191 */     long higher = id + 1L;
/* 143:192 */     long lower = id - 1L;
/* 144:193 */     ListIterator<Segment> iter = this.segments.listIterator();
/* 145:195 */     while (iter.hasNext())
/* 146:    */     {
/* 147:196 */       Segment segment = (Segment)iter.next();
/* 148:197 */       if (segment.end >= lower)
/* 149:    */       {
/* 150:200 */         if (segment.start > higher)
/* 151:    */         {
/* 152:201 */           iter.previous();
/* 153:202 */           iter.add(new Segment(id, id));
/* 154:203 */           return true;
/* 155:    */         }
/* 156:205 */         if (segment.start == higher)
/* 157:    */         {
/* 158:206 */           segment.start = id;
/* 159:207 */           return true;
/* 160:    */         }
/* 161:209 */         if (segment.end == lower)
/* 162:    */         {
/* 163:210 */           segment.end = id;
/* 164:212 */           if (iter.hasNext())
/* 165:    */           {
/* 166:213 */             Segment next = (Segment)iter.next();
/* 167:214 */             if (next.start == segment.end + 1L)
/* 168:    */             {
/* 169:215 */               segment.end = next.end;
/* 170:216 */               iter.remove();
/* 171:    */             }
/* 172:    */           }
/* 173:219 */           return true;
/* 174:    */         }
/* 175:    */       }
/* 176:    */     }
/* 177:226 */     return false;
/* 178:    */   }
/* 179:    */   
/* 180:    */   public long getRemainingIdentifiers()
/* 181:    */   {
/* 182:230 */     long result = 0L;
/* 183:231 */     for (Segment segment : this.segments)
/* 184:    */     {
/* 185:232 */       result -= segment.start;
/* 186:233 */       result = result + segment.end + 1L;
/* 187:    */     }
/* 188:235 */     return result;
/* 189:    */   }
/* 190:    */   
/* 191:    */   private void verifyIdentifiersLeft()
/* 192:    */   {
/* 193:242 */     if (this.segments.isEmpty()) {
/* 194:243 */       throw new IllegalStateException("No identifiers left");
/* 195:    */     }
/* 196:    */   }
/* 197:    */   
/* 198:    */   private static class Segment
/* 199:    */   {
/* 200:    */     public long start;
/* 201:    */     public long end;
/* 202:    */     
/* 203:    */     public Segment(long start, long end)
/* 204:    */     {
/* 205:250 */       this.start = start;
/* 206:251 */       this.end = end;
/* 207:    */     }
/* 208:    */     
/* 209:    */     public String toString()
/* 210:    */     {
/* 211:263 */       return "[" + this.start + "; " + this.end + "]";
/* 212:    */     }
/* 213:    */   }
/* 214:    */ }


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.util.IdentifierManager
 * JD-Core Version:    0.7.0.1
 */