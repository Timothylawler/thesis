import React, { Component } from 'react';

import CameraView from './nativeViews/cameraView';

import Dimensions from 'Dimensions';

import {
	Text,
	View,
	Platform,
} from 'react-native';

//	Styles
import containerStyle from '../styles/container';

class CameraPhoto extends Component {


	
	render() {
		var wHeight = Dimensions.get('window').height -64 ;
		var wWidth = Dimensions.get('window').width;
		console.log("Width: ", wWidth , ", Height: ", wHeight);
		//console.log("CAMERAPHOTO: ", CameraPhotoManager)
		//	Ok so for some reason this top padding is wonky. Apparently the padding decreases as the width of the camera
		//		frame increases? at 325 units width paddign top is set to 38. at 350 width the padding top is good at 55...
		return (
			<View style={{height: wHeight, width: wWidth, padding: 8, paddingTop: 55}}>
				<CameraView style={{flex: 1}}/>
			</View>
		);
	}
}

export default CameraPhoto;