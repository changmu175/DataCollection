package org.apache.poi.sl.usermodel;

import java.awt.Dimension;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public abstract interface SlideShow<S extends Shape<S, P>, P extends TextParagraph<S, P, ?>>
  extends Closeable
{
  public abstract Slide<S, P> createSlide()
    throws IOException;
  
  public abstract List<? extends Slide<S, P>> getSlides();
  
  public abstract MasterSheet<S, P> createMasterSheet()
    throws IOException;
  
  public abstract List<? extends MasterSheet<S, P>> getSlideMasters();
  
  public abstract Resources getResources();
  
  public abstract Dimension getPageSize();
  
  public abstract void setPageSize(Dimension paramDimension);
  
  public abstract List<? extends PictureData> getPictureData();
  
  public abstract PictureData addPicture(byte[] paramArrayOfByte, PictureData.PictureType paramPictureType)
    throws IOException;
  
  public abstract PictureData addPicture(InputStream paramInputStream, PictureData.PictureType paramPictureType)
    throws IOException;
  
  public abstract PictureData addPicture(File paramFile, PictureData.PictureType paramPictureType)
    throws IOException;
  
  public abstract PictureData findPictureData(byte[] paramArrayOfByte);
  
  public abstract void write(OutputStream paramOutputStream)
    throws IOException;
}


/* Location:           F:\poi-3.17.jar
 * Qualified Name:     org.apache.poi.sl.usermodel.SlideShow
 * JD-Core Version:    0.7.0.1
 */