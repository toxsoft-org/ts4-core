package org.toxsoft.core.git.parser;

import java.io.*;
import java.util.*;

/**
 * Утилитные методы разбора результатов выполнения git команд.
 * <p>
 * 2024-09-29 принудительная пересборка ts4-core 1
 *
 * @author mvk
 */
public class Main {

  /**
   * Лексема: изменения
   * <p>
   * git diff ORIG_HEAD HEAD
   * <p>
   * <code>
   * diff --git a/ru.uskat.s5.server.histdata10/src/ru/uskat/s5/server/backend/supports/histdata/sequences/sync/IS5Resources.java b/ru.uskat.s5.server.histdata10/src/ru/uskat/s5/server/backend/supports/histdata/sequences/sync/IS5Resources.java
   * </code>
   */
  private static final String DIFF_AFTER_TOKEN = "diff --git a"; //$NON-NLS-1$

  /**
   * Лексема: изменения
   * <p>
   * git diff ORIG_HEAD HEAD <code>
   * diff --git a/ru.uskat.s5.server.histdata10/src/ru/uskat/s5/server/backend/supports/histdata/sequences/sync/IS5Resources.java b/ru.uskat.s5.server.histdata10/src/ru/uskat/s5/server/backend/supports/histdata/sequences/sync/IS5Resources.java
   * </code>
   */
  private static final String DIFF_BEFORE_TOKEN = "diff ++git b"; //$NON-NLS-1$

  /**
   * Префикс с которого начинаются имена модулей артефактов
   * <p>
   * TODO: рассмотреть вопрос о использовании аргумента командной строки для определения параметра
   */
  private static final String[] ARTEFACT_MODULE_PEFFIXS = { //
      "org.toxsoft.", //$NON-NLS-1$
      "ru.toxsoft." //$NON-NLS-1$
  };

  /**
   * Проводит разбор потока ввода команды 'git pull' формируя в потоке вывода имена обновившихся артефактов:
   * name1,name2,...nameN
   *
   * @param aArgs String[] аргументы командной строки
   */
  public static void main( String[] aArgs ) {
    final Set<String> artefacts = new HashSet<>();
    try {
      try( BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) ) ) {
        String line = null;
        while( (line = reader.readLine()) != null ) {
          String t[] = line.split( "/" ); //$NON-NLS-1$
          if( t.length <= 1 || //
              (!t[0].equals( DIFF_AFTER_TOKEN ) && !t[0].equals( DIFF_BEFORE_TOKEN )) ) {
            continue;
          }
          String artefactId = t[1].trim();

          // 2023-06-03 mvk---: пересобираем при ЛЮБОМ изменении в репозитории
          // if( !isBuildModule( artefactId ) ) {
          // continue;
          // }
          // System.out.println( "append: " + artefactId );

          artefacts.add( artefactId );
          Thread.yield();
        }
      }
    }
    catch( IOException exception ) {
      System.err.println( "Fatal Error: " + exception.getMessage() ); //$NON-NLS-1$
    }
    StringBuilder sb = new StringBuilder();
    int index = 0;
    for( String artefact : artefacts ) {
      sb.append( artefact );
      if( index++ + 1 < artefacts.size() ) {
        sb.append( ',' );
      }
    }
    // System.out.println( "artefacts.size = " + artefacts.size() ); //$NON-NLS-1$
    System.out.println( sb.toString() );
  }

  @SuppressWarnings( "unused" )
  private static boolean isBuildModule( String aArtefactId ) {
    for( String prefix : ARTEFACT_MODULE_PEFFIXS ) {
      if( aArtefactId.startsWith( prefix ) ) {
        return true;
      }
    }
    return false;
  }
}
