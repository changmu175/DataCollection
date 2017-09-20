package org.apache.poi.openxml4j.opc;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;

public abstract interface RelationshipSource
{
  public abstract PackageRelationship addRelationship(PackagePartName paramPackagePartName, TargetMode paramTargetMode, String paramString);
  
  public abstract PackageRelationship addRelationship(PackagePartName paramPackagePartName, TargetMode paramTargetMode, String paramString1, String paramString2);
  
  public abstract PackageRelationship addExternalRelationship(String paramString1, String paramString2);
  
  public abstract PackageRelationship addExternalRelationship(String paramString1, String paramString2, String paramString3);
  
  public abstract void clearRelationships();
  
  public abstract void removeRelationship(String paramString);
  
  public abstract PackageRelationshipCollection getRelationships()
    throws InvalidFormatException, OpenXML4JException;
  
  public abstract PackageRelationship getRelationship(String paramString);
  
  public abstract PackageRelationshipCollection getRelationshipsByType(String paramString)
    throws InvalidFormatException, IllegalArgumentException, OpenXML4JException;
  
  public abstract boolean hasRelationships();
  
  public abstract boolean isRelationshipExists(PackageRelationship paramPackageRelationship);
}


/* Location:           F:\poi-ooxml-3.17.jar
 * Qualified Name:     org.apache.poi.openxml4j.opc.RelationshipSource
 * JD-Core Version:    0.7.0.1
 */