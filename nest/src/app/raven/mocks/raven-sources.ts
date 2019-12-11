/**
 * Copyright 2018, by the California Institute of Technology. ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
 * Any commercial use must be negotiated with the Office of Technology Transfer at the California Institute of Technology.
 * This software may be subject to U.S. export control laws and regulations.
 * By accepting this document, the user agrees to comply with all applicable U.S. export laws and regulations.
 * User has the responsibility to obtain export licenses, or other export authority as may be required
 * before exporting such information to foreign countries or providing access to foreign persons
 */

import { StringTMap } from '../models';
import {
  RavenBaseSource,
  RavenCustomFilterSource,
  RavenCustomGraphableSource,
  RavenFilterSource,
  RavenGraphableFilterSource,
  RavenGraphableSource,
  RavenSource,
} from '../models';

export const rootSource: RavenSource = {
  actions: [],
  childIds: [],
  content: [],
  dbType: '',
  draggable: false,
  expandable: false,
  expanded: false,
  fileMetadata: {
    createdBy: '',
    createdOn: '',
    customMetadata: null,
    fileType: '',
    lastModified: '',
    permissions: '',
  },
  icon: '',
  id: '/',
  isServer: false,
  kind: '',
  label: 'root',
  menu: false,
  name: 'root',
  openable: false,
  opened: false,
  parentId: '',
  pathInFile: '',
  permissions: '',
  pinnable: false,
  pinned: false,
  subBandIds: [],
  subKind: '',
  type: '',
  url: '',
};

export const categorySource: RavenBaseSource = {
  actions: [],
  childIds: [],
  content: [],
  dbType: '',
  draggable: false,
  expandable: true,
  expanded: false,
  fileMetadata: {
    createdBy: '',
    createdOn: '',
    customMetadata: null,
    fileType: '',
    lastModified: '',
    permissions: '',
  },
  icon: '',
  id: '/SequenceTracker/Metadata',
  isServer: false,
  kind: 'fs_category',
  label: 'Metadata',
  menu: false,
  name: 'Metadata',
  openable: false,
  parentId: '/SequenceTracker',
  pathInFile: '',
  permissions: '',
  pinnable: false,
  pinned: false,
  subBandIds: [],
  subKind: '',
  type: '',
  url: '',
};

export const childSource: RavenSource = {
  actions: [],
  childIds: [],
  content: [],
  dbType: '',
  draggable: false,
  expandable: true,
  expanded: false,
  fileMetadata: {
    createdBy: '',
    createdOn: '',
    customMetadata: null,
    fileType: '',
    lastModified: '',
    permissions: '',
  },
  icon: '',
  id: '/child',
  isServer: false,
  kind: 'db',
  label: 'test-child-source',
  menu: true,
  name: 'test-child-source',
  openable: false,
  opened: false,
  parentId: '/',
  pathInFile: '',
  permissions: '',
  pinnable: true,
  pinned: false,
  subBandIds: [],
  subKind: '',
  type: '',
  url: '',
};

export const customFilterSource: RavenCustomFilterSource = {
  actions: [],
  childIds: [],
  content: [],
  dbType: '',
  draggable: false,
  expandable: false,
  expanded: false,
  fileMetadata: {
    createdBy: '',
    createdOn: '',
    customMetadata: null,
    fileType: '',
    lastModified: '',
    permissions: '',
  },
  filter: 'jm0132',
  filterSetOf: 'sequenceId',
  filterTarget: 'SequenceTracker',
  icon: '',
  id: '/DKF/sequence',
  isServer: false,
  kind: 'fs_filter',
  label: 'ips',
  menu: false,
  name: 'sequence',
  openable: false,
  opened: false,
  parentId: '/DKF',
  pathInFile: '',
  permissions: '',
  pinnable: false,
  pinned: false,
  subBandIds: [],
  subKind: '',
  type: 'customFilter',
  url: '',
};

export const customGraphableSource: RavenCustomGraphableSource = {
  actions: [],
  arg: 'filter',
  childIds: [],
  content: [],
  dbType: '',
  draggable: false,
  expandable: false,
  expanded: false,
  fileMetadata: {
    createdBy: '',
    createdOn: '',
    customMetadata: null,
    fileType: '',
    lastModified: '',
    permissions: '',
  },
  filterKey: 'command',
  icon: '',
  id: '/DKF/command',
  isServer: false,
  kind: 'fs_filter',
  label: 'ips',
  menu: false,
  name: 'command',
  openable: false,
  parentId: '/DKF',
  pathInFile: '',
  permissions: '',
  pinnable: false,
  pinned: false,
  subBandIds: [],
  subKind: '',
  type: 'customGraphable',
  url: 'https://a/b/c?format=TMS',
};

export const filterSourceLocation: RavenFilterSource = {
  actions: [],
  childIds: [],
  content: [],
  dbType: '',
  draggable: false,
  expandable: false,
  expanded: false,
  fileMetadata: {
    createdBy: '',
    createdOn: '',
    customMetadata: null,
    fileType: '',
    lastModified: '',
    permissions: '',
  },
  filterSetOf: 'meeting',
  filterTarget: '/SequenceTracker',
  icon: '',
  id: '/SequenceTracker/Location',
  isServer: false,
  kind: 'fs_filter',
  label: 'Location',
  menu: false,
  name: 'Location',
  openable: false,
  opened: false,
  parentId: '/SequenceTracker',
  pathInFile: '',
  permissions: '',
  pinnable: false,
  pinned: false,
  subBandIds: [],
  subKind: '',
  type: '',
  url: '',
};

export const filterSourceStatus: RavenFilterSource = {
  actions: [],
  childIds: [],
  content: [],
  dbType: '',
  draggable: false,
  expandable: false,
  expanded: false,
  fileMetadata: {
    createdBy: '',
    createdOn: '',
    customMetadata: null,
    fileType: '',
    lastModified: '',
    permissions: '',
  },
  filterSetOf: 'collection',
  filterTarget: '/SequenceTracker',
  icon: '',
  id: '/SequenceTracker/Status',
  isServer: false,
  kind: 'fs_filter',
  label: 'Status',
  menu: false,
  name: 'Status',
  openable: false,
  opened: false,
  parentId: '/SequenceTracker',
  pathInFile: '',
  permissions: '',
  pinnable: false,
  pinned: false,
  subBandIds: [],
  subKind: '',
  type: '',
  url: '',
};

export const graphableFilterKickoff: RavenGraphableFilterSource = {
  actions: [],
  childIds: [],
  content: [],
  dbType: '',
  draggable: false,
  expandable: false,
  expanded: false,
  fileMetadata: {
    createdBy: '',
    createdOn: '',
    customMetadata: null,
    fileType: '',
    lastModified: '',
    permissions: '',
  },
  filterSetOf: 'events',
  filterTarget: 'SequenceTracker',
  icon: '',
  id: 'SequenceTracker/Kickoff',
  isServer: false,
  kind: 'fs_filter',
  label: 'Kickoff',
  menu: false,
  name: 'Kickoff',
  openable: false,
  opened: false,
  parentId: 'SequenceTracker',
  pathInFile: '',
  permissions: '',
  pinnable: false,
  pinned: false,
  subBandIds: [],
  subKind: '',
  type: 'graphableFilter',
  url: 'https://a/b/c',
};

export const grandChildSource: RavenSource = {
  actions: [],
  childIds: [],
  content: [],
  dbType: '',
  draggable: false,
  expandable: true,
  expanded: false,
  fileMetadata: {
    createdBy: '',
    createdOn: '',
    customMetadata: null,
    fileType: '',
    lastModified: '',
    permissions: '',
  },
  icon: '',
  id: '/child/grandChild',
  isServer: false,
  kind: 'db',
  label: 'test-grand-child-source',
  menu: true,
  name: 'test-grand-child-source',
  openable: false,
  opened: false,
  parentId: '/child',
  pathInFile: '',
  permissions: '',
  pinnable: true,
  pinned: false,
  subBandIds: [],
  subKind: '',
  type: '',
  url: '',
};

export const graphableSource: RavenGraphableSource = {
  actions: [],
  childIds: [],
  content: [],
  dbType: '',
  draggable: false,
  expandable: true,
  expanded: false,
  fileMetadata: {
    createdBy: '',
    createdOn: '',
    customMetadata: null,
    fileType: '',
    lastModified: '',
    permissions: '',
  },
  icon: '',
  id: '/child/grandChild',
  isServer: false,
  kind: 'db',
  label: 'test-grand-child-source',
  menu: true,
  name: 'test-grand-child-source',
  openable: false,
  opened: false,
  parentId: '/child',
  pathInFile: '',
  permissions: '',
  pinnable: true,
  pinned: false,
  subBandIds: [],
  subKind: '',
  type: 'resource',
  url: 'https://a/b/c?name=power&format=TMS&decimate=true',
};

export const treeBySourceId: StringTMap<RavenSource> = {
  '/': {
    ...rootSource,
    childIds: ['/child/0', '/child/1'],
    kind: 'db',
  },
  '/DKF/command': {
    ...customGraphableSource,
  },
  '/SequenceTracker/Location': {
    ...filterSourceLocation,
  },
  '/SequenceTracker/Status': {
    ...filterSourceStatus,
  },
  '/child/0': {
    ...childSource,
    childIds: [],
    id: '/child/0',
    kind: 'file',
    parentId: '/',
  },
  '/child/1': {
    ...childSource,
    childIds: ['/child/child/0'],
    id: '/child/1',
    kind: 'file',
    parentId: '/',
  },
  '/child/child/0': {
    ...childSource,
    childIds: [],
    id: '/child/child/0',
    kind: 'data',
    parentId: '/child/1',
  },
  'SequenceTracker/Kickoff': {
    ...graphableFilterKickoff,
  },
  'SequenceTracker/Location': {
    ...filterSourceLocation,
  },
  'SequenceTracker/Status': {
    ...filterSourceStatus,
  },
};
