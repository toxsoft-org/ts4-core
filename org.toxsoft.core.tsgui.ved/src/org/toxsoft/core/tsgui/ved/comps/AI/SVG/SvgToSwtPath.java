package org.toxsoft.core.tsgui.ved.comps.AI.SVG;

import java.io.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SvgToSwtPath {

  static Text text;

  public static void main( String[] args )
      throws Exception {

    Display display = new Display();
    org.eclipse.swt.widgets.Shell shell = new org.eclipse.swt.widgets.Shell( display );
    shell.setText( "SvgPath to SWT Path converter" );
    shell.setLayout( new FillLayout() );
    shell.setSize( 620, 480 );

    Composite bkPane = new Composite( shell, SWT.NONE );
    bkPane.setLayout( new GridLayout( 2, false ) );

    Button btn = new Button( bkPane, SWT.PUSH );
    btn.setText( "File..." );
    btn.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        FileDialog fd = new FileDialog( shell, SWT.OPEN );
        String str = fd.open();
        if( str != null && !str.isBlank() ) {
          parseFile( str, display );
        }
      }
    } );

    btn = new Button( bkPane, SWT.PUSH );
    btn.setText( "Close" );
    btn.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        shell.close();
      }
    } );

    text = new Text( bkPane, SWT.BORDER | SWT.MULTI );
    text.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 2, 1 ) );

    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

  static void parseFile( String aFilePath, Display aDisplay ) {
    try {
      SvgParser parser = new SvgParser();
      SvgDocument doc = parser.parse( new File( aFilePath ) );
      List<SvgPath> paths = doc.getShapesOfType( SvgPath.class );
      for( int i = 0; i < paths.size(); i++ ) {
        SvgPath p = paths.get( i );
        Path swtPath = SvgArcOptimizer.parse( aDisplay, p.getD() );
        float[] bounds = new float[4];
        swtPath.getBounds( bounds );
        PathData pd = swtPath.getPathData();
        byte[] types = pd.types;
        float[] points = pd.points;
        text.append( "static { " + "\r\n" );
        text.append( "PathData pd = new PathData();" + "\r\n" );
        String str = "pd.types = new byte[] {" + "\r\n";
        for( int j = 0; j < types.length; j++ ) {
          str += types[j];
          if( j < types.length - 1 ) {
            str += ",";
          }
        }
        str += "};" + "\r\n"; // SWT.CR;
        text.append( str );

        str = "pd.points = new float[] {" + "//" + "\r\n";
        for( int j = 0; j < points.length; j++ ) {
          if( j % 2 == 0 ) {
            str += (points[j] - bounds[0]) + "f";
          }
          else {
            str += (points[j] - bounds[1]) + "f";
          }
          if( j < points.length - 1 ) {
            str += ", ";
          }
          if( (j + 1) % 2 == 0 ) {
            str += " //";
            str += "\r\n"; // SWT.CR;
          }
        }
        str += "};" + "\r\n";
        text.append( str );
        text.append( "}" );
      }
    }
    catch( Throwable e ) {
      e.printStackTrace();
    }
  }

}
