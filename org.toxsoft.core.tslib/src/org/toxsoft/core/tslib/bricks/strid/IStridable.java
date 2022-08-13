package org.toxsoft.core.tslib.bricks.strid;

import org.toxsoft.core.tslib.bricks.strid.impl.*;

/**
 * Entitiy distinguished by the unique string identifiers.
 * <p>
 * Stridable entities are mainly designed to be identified by humans hence human-readable name and description are
 * provided.
 * <p>
 * Which kind of ID (IDname or IDpath, see {@link StridUtils}) is used as {@link #id()} identifier depends on
 * application and entity itself. This interface does not differs any of them.
 * <p>
 * Strid (STRID - STRing IDentifier) is string assosiated with entity and assumes following:
 * <ul>
 * <li><b>uniquity</b> - correspondence is set unambiguously between strid and entity in the scope of some context (eg.
 * employees in company, cards in deck, etc.);</li>
 * <li><b>completness</b> - any entity in context has strid. If any entity in scope can be accessed using strid then any
 * other entity in the same scope is acessible with corrsponding and different strid;</li>
 * <li><b>persistensy</b> - correspondence between an entity and strid remains unchanged between program launches, file
 * save and load operations, when transferring between machines. etc. In other words, it is implicitly assumed that
 * there is the rule that uniquely determine how entity strid is formed.
 * </ul>
 * Nearly the ideal example of string identifier is URL - globally uinque address of resourse in internet. Also the good
 * exmple is Java class full name, probably also globally uinque. However <code>tslib</code> implementation strictly
 * specifies syntax of strids. Strid must be IDname or IDpath, see {@link StridUtils}. The {@link #id()} method returns
 * strid identifier generally not suitable for human reading. Human readable (and possibly localizable) strings are
 * returned by {@link #description()} and {@link #nmName()} methods.
 * <p>
 * Note: method {@link #nmName()} is <b>not</b> named as <code>name()</code> to avoid name clash with
 * {@link Enum#name()} when some <b>enum</b> extends this interface. This is two different methods: {@link #nmName()}
 * returns editable human-readable and localizable title of entity while {@link Enum#name()} returns string
 * representation of enum constant as it is declared in java code.
 *
 * @see StridUtils
 * @author hazard157
 */
public interface IStridable {

  /**
   * Identifier having no corresponding entity.
   * <p>
   * Is used for None special case instances. Such instances are just programmatic implementation (instead of
   * <code>null</code>) of absence of entity.
   */
  String NONE_ID = "__none__"; //$NON-NLS-1$

  /**
   * Returns strid (string identifier) of this entity.
   * <p>
   * Always returns syntactically valid IDname or IDpath. For unmutable objects there is no exception from this rule.
   * The only excpetion happens during complex mutable instance building process. In the other words, stridable entity
   * can not be built without valid identifier.
   * <p>
   * Never throws exception. For special case objects may return {@link #NONE_ID}.
   *
   * @return String - strid, string identifier, either IDname or IDpath
   */
  String id();

  /**
   * Returns human-readable name of this entity.
   * <p>
   * Though it is allowed to return empty string it is highly recommended to return meaningful short string.
   * <p>
   * Never throws exeption. Never returns <code>null</code>.
   * <p>
   * The general rule to create human-readable name is to use one to five words (in single line) to describe this
   * entity. Imagine that this string will be used in GUI list widget - one row per entity. So main purpose of
   * {@link #nmName()} is to distinguish entities of the same scope between each other.
   *
   * @return String - human-readable short name
   */
  String nmName();

  /**
   * Returns human-readable description of this entity.
   * <p>
   * Never throws exeption. Never returns <code>null</code>.
   * <p>
   * The general rule to create human-readable description is to use one to five sentences to describe this entity -
   * what it is, how it behaves, etc. Imagine that this string will be used in GUI text widget - one paragraph for
   * entity, or as floating tootip to some item on screen.
   *
   * @return String - human-readable description, may be an empty string
   */
  String description();

  /**
   * Determines if this is None entity.
   * <p>
   * By definitin returns the same value as <code>{@link String#equals id().equals(NONE_ID)}</code>.
   *
   * @return boolean - <code>true</code> if this is None entity
   */
  default boolean isNone() {
    return id().equals( NONE_ID );
  }

}
