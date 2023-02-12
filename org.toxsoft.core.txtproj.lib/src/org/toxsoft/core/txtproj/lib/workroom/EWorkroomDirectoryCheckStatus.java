package org.toxsoft.core.txtproj.lib.workroom;

/**
 * Possible statuses of workroom directory checking status.
 *
 * @author hazard157
 */
enum EWorkroomDirectoryCheckStatus {

  /**
   * Everything is OK, may be started in this directory.
   */
  OK,

  /**
   * An empty directory, it may be initialized and may bve started.
   */
  EMPTY_DIR,

  /**
   * An empty Eclipse project directory.
   * <p>
   * When an empty project of type General - Project is created in Eclipse, a directory is created with a single file
   * ".project". This is often the case when you want to create a workroom as an Eclipse project located in Git.
   * Therefore, such a directory is also equal to empty and you can initialize the workroom in it.
   */
  EMPTY_ECLIPSE_PROJ,

  /**
   * A non-empty directory and it is not a workroom.
   */
  NON_WR,

  /**
   * It looks like a workroom but it is corrupted.
   */
  CORRUPTED,

  /**
   * Busy workroom - other instance of is running in it.
   */
  IS_BUSY,

  /**
   * The directory does not exists.
   */
  NOT_EXISTS,

  /**
   * Directory flavor is not of expected type.
   */
  BAD_FLAVOUR,

  /**
   * This workroom is created by older version of the application.
   */
  OLDER_VERSION,

  /**
   * This workroom is created by newer version of the application.
   */
  NEWER_VERSION,

  /**
   * The directory is not accessible.
   */
  DIR_INACSSABLE
}
