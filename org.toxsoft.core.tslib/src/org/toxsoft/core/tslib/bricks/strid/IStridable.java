package org.toxsoft.core.tslib.bricks.strid;

import org.toxsoft.core.tslib.bricks.strid.impl.*;

/**
 * Entity distinguished by the unique string identifiers.
 * <p>
 * Stridable entities are mainly designed to be identified by humans hence human-readable name and description are
 * provided.
 * <p>
 * Which kind of ID (IDname or IDpath, see {@link StridUtils}) is used as {@link #id()} identifier depends on
 * application and entity itself. This interface does not differs any of them.
 * <p>
 * Strid (STRID - STRing IDentifier) is string associated with entity and assumes following:
 * <ul>
 * <li><b>uniquity</b> - correspondence is set unambiguously between strid and entity in the scope of some context (eg.
 * employees in company, cards in deck, etc.);</li>
 * <li><b>completeness</b> - any entity in context has strid. If any entity in scope can be accessed using strid then
 * any other entity in the same scope is accessible with corresponding and different strid;</li>
 * <li><b>persistence</b> - correspondence between an entity and strid remains unchanged between program launches, file
 * save and load operations, when transferring between machines. etc. In other words, it is implicitly assumed that
 * there is the rule that uniquely determine how entity strid is formed.
 * </ul>
 * Nearly the ideal example of string identifier is URL - globally unique address of resource in Internet. Also the good
 * example is Java class full name, probably also globally unique. However <code>tslib</code> implementation strictly
 * specifies syntax of STRIDs. Strid must be IDname or IDpath, see {@link StridUtils}. The {@link #id()} method returns
 * strid identifier generally not suitable for human reading. Human readable (and possibly localizable) strings are
 * returned by {@link #description()} and {@link #nmName()} methods.
 * <p>
 * Note: method {@link #nmName()} is <b>not</b> named as <code>name()</code> to avoid name clash with
 * {@link Enum#name()} when some <b><code>enum</code></b> extends this interface. This is two different methods:
 * {@link #nmName()} returns editable human-readable and localizable title of entity while {@link Enum#name()} returns
 * string representation of <code>enum</code> constant as it is declared in java code.
 *
 * @see StridUtils
 * @author hazard157
 */
public interface IStridable {

  /**
   * Identifier having no corresponding entity.
   * <p>
   * Used for None special case instances. Such instances are just programmatic implementation (instead of
   * <code>null</code>) of absence of entity. Also may be used as ID of the <code>enum</code>s with meaning of none,
   * nothing, absence of something, etc.
   */
  String NONE_ID = "__none__"; //$NON-NLS-1$

  /**
   * Returns strid (string identifier) of this entity.
   * <p>
   * For completely built objects returns valid IDpath (or IDname). The exception of returning an empty string may
   * happen for special cases instances and such cases must be carefully documented.
   * <p>
   * Never throws an exception. Never returns <code>null</code>.
   *
   * @return String - strid, string identifier, either IDname or IDpath
   */
  String id();

  /**
   * Returns human-readable name of this entity.
   * <p>
   * Though it is allowed to return empty string it is highly recommended to return meaningful short string.
   * <p>
   * Never throws an exception. Never returns <code>null</code>.
   * <p>
   * The general rule creating human-readable name is to use one to five words (in single line) to describe this entity.
   * Imagine that this string will be used in GUI list widget - one row per entity. So main purpose of {@link #nmName()}
   * is to distinguish entities of the same scope between each other.
   *
   * @return String - human-readable short name
   */
  String nmName();

  /**
   * Returns human-readable description of this entity.
   * <p>
   * Never throws an exception. Never returns <code>null</code>.
   * <p>
   * The general rule to create human-readable description is to use one to five sentences to describe this entity -
   * what it is, how it behaves, etc. Imagine that this string will be used in GUI text widget - one paragraph for
   * entity, or as floating tooltip to some item on screen.
   *
   * @return String - human-readable description, may be an empty string
   */
  String description();

  /**
   * Determines if this is None entity.
   * <p>
   * By definition returns the same value as <code>{@link String#equals id().equals(NONE_ID)}</code>.
   *
   * @return boolean - <code>true</code> if this is None entity
   */
  default boolean isNone() {
    return id().equals( NONE_ID );
  }

}
