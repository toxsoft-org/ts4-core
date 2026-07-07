package org.toxsoft.core.tsgui.ved.comps.AI;

//import com.jogamp.opengl.*;
//import com.jogamp.opengl.glu.GLU;

public class TorusGLCanvas {
  // implements GLEventListener {

  // ── Параметры тора ──────────────────────────────────────────────
  // private static final double R = 0.6; // большой радиус
  // private static final double r = 0.25; // малый радиус (трубка)
  // private static final int STEPS_U = 64; // сегменты трубки
  // private static final int STEPS_V = 64; // сегменты вращения
  //
  // // Часть тора: угол v от V_START до V_END (в градусах)
  // private static double V_START_DEG = 0;
  // private static double V_END_DEG = 270; // три четверти тора
  //
  // // ── Вращение мышью ─────────────────────────────────────────────
  // private float rotX = 30f, rotY = 20f;
  // private int lastMouseX, lastMouseY;
  //
  // private GL2 gl;
  // private GLU glu = new GLU();
  //
  // // ──────────────────────────────────────────────────────────────
  // public static void main( String[] args ) {
  // Display display = new Display();
  // Shell shell = new Shell( display );
  // shell.setText( "Часть тора — SWT GLCanvas + JOGL" );
  // shell.setLayout( new FillLayout() );
  // shell.setSize( 800, 600 );
  //
  // // GLData: двойная буферизация + depth buffer
  // GLData data = new GLData();
  // data.doubleBuffer = true;
  // data.depthSize = 24;
  //
  // GLCanvas canvas = new GLCanvas( shell, SWT.NONE, data );
  //
  // TorusGLCanvas renderer = new TorusGLCanvas();
  //
  // // Подключаем JOGL к SWT GLCanvas
  // com.jogamp.opengl.GLProfile profile = com.jogamp.opengl.GLProfile.get( com.jogamp.opengl.GLProfile.GL2 );
  // com.jogamp.opengl.GLCapabilities caps = new com.jogamp.opengl.GLCapabilities( profile );
  // caps.setDoubleBuffered( true );
  //
  // // Используем SWT-адаптер
  // canvas.addListener( SWT.Paint, e -> {
  // canvas.setCurrent();
  // com.jogamp.opengl.GLContext ctx =
  // com.jogamp.opengl.GLDrawableFactory.getFactory( profile ).createExternalGLContext();
  // ctx.makeCurrent();
  // renderer.gl = ctx.getGL().getGL2();
  // renderer.display( null );
  // canvas.swapBuffers();
  // ctx.release();
  // } );
  //
  // // Мышь: вращение
  // canvas.addListener( SWT.MouseDown, e -> {
  // renderer.lastMouseX = e.x;
  // renderer.lastMouseY = e.y;
  // } );
  // canvas.addListener( SWT.MouseMove, e -> {
  // if( (e.stateMask & SWT.BUTTON1) != 0 ) {
  // renderer.rotY += (e.x - renderer.lastMouseX) * 0.5f;
  // renderer.rotX += (e.y - renderer.lastMouseY) * 0.5f;
  // renderer.lastMouseX = e.x;
  // renderer.lastMouseY = e.y;
  // canvas.redraw();
  // }
  // } );
  //
  // shell.open();
  // while( !shell.isDisposed() ) {
  // if( !display.readAndDispatch() ) {
  // display.sleep();
  // }
  // }
  // display.dispose();
  // }
  //
  // // ── GLEventListener ───────────────────────────────────────────
  //
  // @Override
  // public void init( GLAutoDrawable drawable ) {
  // gl = drawable.getGL().getGL2();
  //
  // gl.glEnable( GL2.GL_DEPTH_TEST );
  // gl.glEnable( GL2.GL_LIGHTING );
  // gl.glEnable( GL2.GL_LIGHT0 );
  // gl.glEnable( GL2.GL_COLOR_MATERIAL );
  // gl.glShadeModel( GL2.GL_SMOOTH );
  //
  // // Свет
  // float[] lightPos = { 2f, 3f, 4f, 1f };
  // float[] ambient = { 0.2f, 0.2f, 0.2f, 1f };
  // float[] diffuse = { 0.9f, 0.9f, 0.9f, 1f };
  // float[] specular = { 1.0f, 1.0f, 1.0f, 1f };
  // gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0 );
  // gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0 );
  // gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0 );
  // gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular, 0 );
  //
  // // Материал
  // float[] matSpec = { 0.8f, 0.8f, 0.8f, 1f };
  // gl.glMaterialfv( GL2.GL_FRONT, GL2.GL_SPECULAR, matSpec, 0 );
  // gl.glMaterialf( GL2.GL_FRONT, GL2.GL_SHININESS, 64f );
  //
  // gl.glClearColor( 0.1f, 0.1f, 0.15f, 1f );
  // }
  //
  // @Override
  // public void reshape( GLAutoDrawable drawable, int x, int y, int w, int h ) {
  // gl = drawable.getGL().getGL2();
  // gl.glViewport( 0, 0, w, h );
  //
  // gl.glMatrixMode( GL2.GL_PROJECTION );
  // gl.glLoadIdentity();
  // glu.gluPerspective( 45.0, (double)w / h, 0.1, 100.0 );
  //
  // gl.glMatrixMode( GL2.GL_MODELVIEW );
  // }
  //
  // @Override
  // public void display( GLAutoDrawable drawable ) {
  // if( drawable != null ) {
  // gl = drawable.getGL().getGL2();
  // }
  //
  // gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
  // gl.glLoadIdentity();
  //
  // // Камера
  // glu.gluLookAt( 0, 0, 3, 0, 0, 0, 0, 1, 0 );
  //
  // // Вращение
  // gl.glRotatef( rotX, 1, 0, 0 );
  // gl.glRotatef( rotY, 0, 1, 0 );
  //
  // // Цвет тора
  // gl.glColor3f( 0.2f, 0.7f, 0.9f );
  //
  // drawPartialTorus( gl );
  // }
  //
  // @Override
  // public void dispose( GLAutoDrawable drawable ) {
  // }
  //
  // // ── Рисование части тора ──────────────────────────────────────
  //
  // private void drawPartialTorus( GL2 gl ) {
  // double vStart = Math.toRadians( V_START_DEG );
  // double vEnd = Math.toRadians( V_END_DEG );
  // double stepV = (vEnd - vStart) / STEPS_V;
  // double stepU = 2 * Math.PI / STEPS_U;
  //
  // for( int j = 0; j < STEPS_V; j++ ) {
  // double v0 = vStart + j * stepV;
  // double v1 = vStart + (j + 1) * stepV;
  //
  // gl.glBegin( GL2.GL_TRIANGLE_STRIP );
  //
  // for( int i = 0; i <= STEPS_U; i++ ) {
  // double u = i * stepU;
  //
  // // Вершина при v0
  // double[] p0 = torusPoint( u, v0 );
  // double[] n0 = torusNormal( u, v0 );
  // gl.glNormal3d( n0[0], n0[1], n0[2] );
  // gl.glVertex3d( p0[0], p0[1], p0[2] );
  //
  // // Вершина при v1
  // double[] p1 = torusPoint( u, v1 );
  // double[] n1 = torusNormal( u, v1 );
  // gl.glNormal3d( n1[0], n1[1], n1[2] );
  // gl.glVertex3d( p1[0], p1[1], p1[2] );
  // }
  //
  // gl.glEnd();
  // }
  //
  // // Торцевые крышки (если тор не полный)
  // if( V_END_DEG - V_START_DEG < 360.0 ) {
  // drawEndCap( gl, vStart );
  // drawEndCap( gl, vEnd );
  // }
  // }
  //
  // /** Координата точки на торе */
  // private double[] torusPoint( double u, double v ) {
  // double x = (R + r * Math.cos( u )) * Math.cos( v );
  // double y = (R + r * Math.cos( u )) * Math.sin( v );
  // double z = r * Math.sin( u );
  // return new double[] { x, y, z };
  // }
  //
  // /** Нормаль в точке тора */
  // private double[] torusNormal( double u, double v ) {
  // double nx = Math.cos( u ) * Math.cos( v );
  // double ny = Math.cos( u ) * Math.sin( v );
  // double nz = Math.sin( u );
  // return new double[] { nx, ny, nz };
  // }
  //
  // /** Торцевая крышка на заданном угле v */
  // private void drawEndCap( GL2 gl, double v ) {
  // // Нормаль торца направлена вдоль касательной к v
  // double nx = -Math.sin( v );
  // double ny = Math.cos( v );
  // gl.glNormal3d( nx, ny, 0 );
  //
  // gl.glBegin( GL2.GL_TRIANGLE_FAN );
  // // Центр крышки — на оси большого кольца
  // gl.glVertex3d( R * Math.cos( v ), R * Math.sin( v ), 0 );
  //
  // for( int i = 0; i <= STEPS_U; i++ ) {
  // double u = i * 2 * Math.PI / STEPS_U;
  // double[] p = torusPoint( u, v );
  // gl.glVertex3d( p[0], p[1], p[2] );
  // }
  // gl.glEnd();
  // }
}
