/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Text,
  View
} from 'react-native';

export default class Test extends Component {
  constructor(props){
    super(props);
    this.state ={
      text: props.text
    };
  }
  
  
  render() {
    return (
      <View>
        <Text>
          {this.state.text}
        </Text>
      </View>
    );
  }
}


