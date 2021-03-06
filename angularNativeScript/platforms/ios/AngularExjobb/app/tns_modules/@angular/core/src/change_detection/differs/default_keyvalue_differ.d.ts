import { KeyValueChangeRecord, KeyValueChanges, KeyValueDiffer, KeyValueDifferFactory } from './keyvalue_differs';
export declare class DefaultKeyValueDifferFactory<K, V> implements KeyValueDifferFactory {
    constructor();
    supports(obj: any): boolean;
    create<K, V>(): DefaultKeyValueDiffer<K, V>;
}
export declare class DefaultKeyValueDiffer<K, V> implements KeyValueDiffer<K, V>, KeyValueChanges<K, V> {
    private _records;
    private _mapHead;
    private _previousMapHead;
    private _changesHead;
    private _changesTail;
    private _additionsHead;
    private _additionsTail;
    private _removalsHead;
    private _removalsTail;
    readonly isDirty: boolean;
    forEachItem(fn: (r: KeyValueChangeRecord<K, V>) => void): void;
    forEachPreviousItem(fn: (r: KeyValueChangeRecord<K, V>) => void): void;
    forEachChangedItem(fn: (r: KeyValueChangeRecord<K, V>) => void): void;
    forEachAddedItem(fn: (r: KeyValueChangeRecord<K, V>) => void): void;
    forEachRemovedItem(fn: (r: KeyValueChangeRecord<K, V>) => void): void;
    diff(map: Map<any, any> | {
        [k: string]: any;
    }): any;
    onDestroy(): void;
    check(map: Map<any, any> | {
        [k: string]: any;
    }): boolean;
    private _truncate(lastRecord, record);
    private _maybeAddToChanges(record, newValue);
    private _isInRemovals(record);
    private _addToRemovals(record);
    private _removeFromSeq(prev, record);
    private _removeFromRemovals(record);
    private _addToAdditions(record);
    private _addToChanges(record);
    toString(): string;
}
