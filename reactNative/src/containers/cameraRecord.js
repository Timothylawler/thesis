import React, { Component } from 'react';

import CameraRecordView from './nativeViews/cameraRecordView';

import Dimensions from 'Dimensions';

import {
	Text,
	View
} from 'react-native';

//	Styles
import containerStyle from '../styles/container';

class CameraRecord extends Component {
	render() {
		var wHeight = Dimensions.get('window').height -64 ;
		var wWidth = Dimensions.get('window').width;
		return (
			<View style={{height: wHeight, width: wWidth, padding: 8, paddingTop: 55}}>

				<CameraRecordView style={{flex: 1}}/>
			</View>
		);
	}
}

export default CameraRecord;