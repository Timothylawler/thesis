//import { PropTypes, Component } from 'react';
import React, { Component } from 'react';
import { requireNativeComponent, View } from 'react-native';

var iface = {
	
	propTypes:{
		...View.propTypes
	}
}

class CameraView extends Component{
	render(){
		return(
			<CameraPhotoManager {...this.props} />
		)
	}
}

CameraView.propTypes = {
	...View.propTypes
}

var CameraPhotoManager = requireNativeComponent('RCTCameraView', CameraView);

export default CameraView;
//module.exports = CameraPhotoManager;