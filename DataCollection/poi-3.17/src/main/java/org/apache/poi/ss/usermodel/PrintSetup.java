package org.apache.poi.ss.usermodel;

public abstract interface PrintSetup
{
  public static final short PRINTER_DEFAULT_PAPERSIZE = 0;
  public static final short LETTER_PAPERSIZE = 1;
  public static final short LETTER_SMALL_PAGESIZE = 2;
  public static final short TABLOID_PAPERSIZE = 3;
  public static final short LEDGER_PAPERSIZE = 4;
  public static final short LEGAL_PAPERSIZE = 5;
  public static final short STATEMENT_PAPERSIZE = 6;
  public static final short EXECUTIVE_PAPERSIZE = 7;
  public static final short A3_PAPERSIZE = 8;
  public static final short A4_PAPERSIZE = 9;
  public static final short A4_SMALL_PAPERSIZE = 10;
  public static final short A5_PAPERSIZE = 11;
  public static final short B4_PAPERSIZE = 12;
  public static final short B5_PAPERSIZE = 13;
  public static final short FOLIO8_PAPERSIZE = 14;
  public static final short QUARTO_PAPERSIZE = 15;
  public static final short TEN_BY_FOURTEEN_PAPERSIZE = 16;
  public static final short ELEVEN_BY_SEVENTEEN_PAPERSIZE = 17;
  public static final short NOTE8_PAPERSIZE = 18;
  public static final short ENVELOPE_9_PAPERSIZE = 19;
  public static final short ENVELOPE_10_PAPERSIZE = 20;
  public static final short ENVELOPE_DL_PAPERSIZE = 27;
  public static final short ENVELOPE_CS_PAPERSIZE = 28;
  public static final short ENVELOPE_C5_PAPERSIZE = 28;
  public static final short ENVELOPE_C3_PAPERSIZE = 29;
  public static final short ENVELOPE_C4_PAPERSIZE = 30;
  public static final short ENVELOPE_C6_PAPERSIZE = 31;
  public static final short ENVELOPE_MONARCH_PAPERSIZE = 37;
  public static final short A4_EXTRA_PAPERSIZE = 53;
  public static final short A4_TRANSVERSE_PAPERSIZE = 55;
  public static final short A4_PLUS_PAPERSIZE = 60;
  public static final short LETTER_ROTATED_PAPERSIZE = 75;
  public static final short A4_ROTATED_PAPERSIZE = 77;
  
  public abstract void setPaperSize(short paramShort);
  
  public abstract void setScale(short paramShort);
  
  public abstract void setPageStart(short paramShort);
  
  public abstract void setFitWidth(short paramShort);
  
  public abstract void setFitHeight(short paramShort);
  
  public abstract void setLeftToRight(boolean paramBoolean);
  
  public abstract void setLandscape(boolean paramBoolean);
  
  public abstract void setValidSettings(boolean paramBoolean);
  
  public abstract void setNoColor(boolean paramBoolean);
  
  public abstract void setDraft(boolean paramBoolean);
  
  public abstract void setNotes(boolean paramBoolean);
  
  public abstract void setNoOrientation(boolean paramBoolean);
  
  public abstract void setUsePage(boolean paramBoolean);
  
  public abstract void setHResolution(short paramShort);
  
  public abstract void setVResolution(short paramShort);
  
  public abstract void setHeaderMargin(double paramDouble);
  
  public abstract void setFooterMargin(double paramDouble);
  
  public abstract void setCopies(short paramShort);
  
  public abstract short getPaperSize();
  
  public abstract short getScale();
  
  public abstract short getPageStart();
  
  public abstract short getFitWidth();
  
  public abstract short getFitHeight();
  
  public abstract boolean getLeftToRight();
  
  public abstract boolean getLandscape();
  
  public abstract boolean getValidSettings();
  
  public abstract boolean getNoColor();
  
  public abstract boolean getDraft();
  
  public abstract boolean getNotes();
  
  public abstract boolean getNoOrientation();
  
  public abstract boolean getUsePage();
  
  public abstract short getHResolution();
  
  public abstract short getVResolution();
  
  public abstract double getHeaderMargin();
  
  public abstract double getFooterMargin();
  
  public abstract short getCopies();
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.ss.usermodel.PrintSetup
 * JD-Core Version:    0.7.0.1
 */