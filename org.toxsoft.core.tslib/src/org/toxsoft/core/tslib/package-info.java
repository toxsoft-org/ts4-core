/**
 * <b>tslib</b> - is the very base library of all <i>toxsoft.org</i> frameworks.
 * <p>
 * The library is designed to solve similar tasks (often small and simple, but significant) in the same ways, using the
 * same classes, interfaces and mini-frameworks given in the <code>tslib</code>.
 * <p>
 * Main parts of <code>tslib</code> are:
 * <ul>
 * <li><b>collections</b> - {@link org.toxsoft.core.tslib.coll.IList IList} and {@link org.toxsoft.core.tslib.coll.IMap
 * IMap} collections used instead of <code>java.util.*</code> collections. Primitive type collections like
 * {@link org.toxsoft.core.tslib.coll.primtypes.ILongList ILongList} or
 * {@link org.toxsoft.core.tslib.coll.primtypes.IStringMap IStringMap} are included. Some additional abilities like
 * synchronized (thread-safe) {@link org.toxsoft.core.tslib.coll.basis.ITsSynchronizedCollectionWrapper
 * ITsSynchronizedCollectionTag} and MVC model supporting
 * {@link org.toxsoft.core.tslib.coll.notifier.basis.ITsNotifierCollection ITsNotifierCollection} collections are added.
 * Take a look at {@link org.toxsoft.core.tslib.coll.helpers.IListReorderer IListReorderer} or
 * {@link org.toxsoft.core.tslib.coll.derivative.IQueue IQueue} also;</li>
 * <li><b>av</b> - atomic values {@link org.toxsoft.core.tslib.av.IAtomicValue IAtomicValue} and corrsponding
 * infrastructure includes:</li>
 * <ul>
 * <li><b>opset</b> - is powerful identified options container {@link org.toxsoft.core.tslib.av.opset.IOptionSet};</li>
 * <li><b>metainfo</b> - meta information about atomic values includes
 * {@link org.toxsoft.core.tslib.av.metainfo.IDataType} type constraints set and
 * {@link org.toxsoft.core.tslib.av.metainfo.IDataDef} to manipulate options;</li>
 * <li><b>validation</b> - is set of {@link org.toxsoft.core.tslib.bricks.validator.ITsValidator} implementations for
 * atomic values;</li>
 * <li><b>other</b> - other abilities includes math operations support,
 * {@link org.toxsoft.core.tslib.av.temporal.ITemporalAtomicValue}, {@link org.toxsoft.core.tslib.av.misc.AvTextParser}
 * to interpret human readable text as atomic values and more.</li>
 * </ul>
 * <li><b>gw</b> - green world concept basics like {@link org.toxsoft.core.tslib.gw.gwid.Gwid Gwid};</li>
 * <li>basic building <b>bricks</b> are some commonly used concepts designed in form of mini-frameworks:</li>
 * <ul>
 * <li><b>strid</b> - string identidfied entities {@link org.toxsoft.core.tslib.bricks.strid.IStridable IStridable}
 * framework;</li>
 * <li><b>strio</b> - text-oriented I/O streams like {@link org.toxsoft.core.tslib.bricks.strio.IStrioWriter
 * IStrioWriter};</li>
 * <li><b>keepers</b> - strio-based serialization support both for unmutable entities via
 * {@link org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper IEntityKeeper} and mutable
 * {@link org.toxsoft.core.tslib.bricks.keeper.IKeepableEntity IKeepableEntity};</li>
 * <li><b>time</b> - real-time basicis like {@link org.toxsoft.core.tslib.bricks.time.ITemporal ITemporal} or
 * {@link org.toxsoft.core.tslib.bricks.time.IQueryInterval IQueryInterval};</li>
 * <li><b>validator</b> - any values/entities validation support includes concepts of validation
 * {@link org.toxsoft.core.tslib.bricks.validator.ITsValidator ITsValidator} and validation result
 * {@link org.toxsoft.core.tslib.bricks.validator.ValidationResult ValidationResult};</li>
 * <li><b>events/messages</b> - general eventing/messaging support
 * {@link org.toxsoft.core.tslib.bricks.events.ITsEventer} for listeners like
 * {@link org.toxsoft.core.tslib.bricks.events.change.IGenericChangeEventer IGenericChangeEventer};</li>
 * <li><b>filtering</b> - general filter {@link org.toxsoft.core.tslib.bricks.filter.ITsFilter ITsFilter} and parameters
 * for filter building - {@link org.toxsoft.core.tslib.bricks.filter.ITsSingleFilterParams ITsSingleFilterParams};</li>
 * <li><b>application</b> - context {@link org.toxsoft.core.tslib.bricks.ctx.ITsContext ITsContext} and preferences
 * {@link org.toxsoft.core.tslib.bricks.apprefs.IAppPreferences IAppPreferences};</li>
 * <li><b>geometry</b> - basics {@link org.toxsoft.core.tslib.bricks.geometry.ITsPoint} and
 * {@link org.toxsoft.core.tslib.bricks.geometry.ITsRectangle};</li>
 * <li><b>filebound</b> - {@link org.toxsoft.core.tslib.bricks.filebound.IKeepedContentFileBound} connects some editable
 * content to the files and makes easy to implement GUI commands New/Open/Save/SaveAs;</li>
 * </ul>
 * <li><b>utils</b> - contains helper classes and methods:</li>
 * <ul>
 * <li><b>exceptions</b> - tslib specific exceptions add timestamp to the exception instance;</li>
 * <li><b>files</b> - helpers methods in {@link org.toxsoft.core.tslib.utils.files.TsFileUtils TsFileUtils} and
 * {@link org.toxsoft.core.tslib.utils.files.TsFileFilter TsFileFilter} to work with {@link java.io.File}s;</li>
 * <li><b>logging</b> - simple logging using {@link org.toxsoft.core.tslib.utils.logs.ILogger ILogger}. Default logging
 * is used inside tslib library;</li>
 * <li><b>value-objects</b> - value objects serialization support using
 * {@link org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper IEntityKeeper} is widely used in ts libraries;</li>
 * <li><b>diff</b> - similar containers compaison utilities;</li>
 * <li><b>prog-args</b> - simple UNIX-like command line arguments supporting
 * {@link org.toxsoft.core.tslib.utils.progargs.ProgramArgs} class is introduced;</li>
 * <li>many other helper interfaces, class and methods may be found in <code>org.toxsoft.utils</code> package.</li>
 * </ul>
 * </ul>
 */
package org.toxsoft.core.tslib;
