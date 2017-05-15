import React, { Component } from 'react';

import {
	StyleSheet,
	Text,
	View,
	Button
} from 'react-native';

//	App
import TutorialApp from '../components/tutorialApp';
import HardwareScene from './hardwareScene';
import FlatListList from './flatList';
import Contacts from './contacts';
import CameraPhoto from './cameraPhoto';
import CameraRecord from './cameraRecord';

//	Navigation
import { Router, Scene, Actions, ActionConst } from 'react-native-router-flux';


// Redux
import { createStore, combineReducers } from 'redux';
import { Provider, connect } from 'react-redux';

import * as lla from '../actions/longListActions';

//	Reducers
import counter from '../reducers/counter';
import sceneReducer from '../reducers/sceneReducer';
import LongListReducer from '../reducers/longListReducer';
import GeoReducer from '../reducers/geoReducer';
import CarrierReducer from '../reducers/carrierReducer';
import NetworkInfoReducer from '../reducers/networkInfoReducer';
import PhoneStateReducer from '../reducers/phoneStateReducer';
import TextMessageReducer from '../reducers/textMessageReducer';
import PickedContactReducer from '../reducers/pickedContactReducer';

const scenes = Actions.create(
	<Scene key="root">
			<Scene key="HardwareScene" component={HardwareScene} title="Hardware" initial={true} />
			<Scene key="LongListScene" component={FlatListList} title="LongList" onRight={() => {
					store.dispatch(lla.addItem({
						firstName: "First name", lastName: "Last name"
					}))
					//Actions.pop();
				}} rightTitle="Add" />
			<Scene key="ContactsScene" component={Contacts} title="Contacts" />
			<Scene key="Camera_PhotoScene" component={CameraPhoto} title="CameraPhoto" />
			<Scene key="Camera_RecordScene" component={CameraRecord} title="CameraRecord" />
			<Scene key="pageOne" component={TutorialApp} title="Hello" />
	</Scene>
);

/**
 * create a combined store of all reducers
 */
let store = createStore(combineReducers({ 
	counter, 
	sceneReducer, 
	LongListReducer, 
	GeoReducer,
	CarrierReducer,
	NetworkInfoReducer,
	PhoneStateReducer,
	TextMessageReducer,
	PickedContactReducer
}));

class App extends Component {
	render() {
		const ConnectedRouter = connect()(Router);
		
		return (
			<Provider store={store}>
				<ConnectedRouter scenes={scenes} />
			</Provider>

		);
	}
}

//<TutorialApp/>

export default App;