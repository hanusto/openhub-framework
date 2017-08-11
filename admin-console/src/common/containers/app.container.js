import React, { Component, PropTypes } from 'react'
import { Router, hashHistory } from 'react-router'
import ReduxToastr from 'react-redux-toastr'
import { Provider } from 'react-redux'

class AppContainer extends Component {
  static propTypes = {
    routes: PropTypes.object.isRequired,
    store: PropTypes.object.isRequired
  }

  shouldComponentUpdate () {
    return false
  }

  render () {
    const { routes, store } = this.props

    return (
      <Provider store={store} >
        <div style={{ height: '100%' }} >
          <ReduxToastr
            timeOut={4000}
            newestOnTop={false}
            position='top-right'
            transitionIn='fadeIn'
            transitionOut='fadeOut'
            progressBar
          />
          <Router history={hashHistory} children={routes} />
        </div >
      </Provider >
    )
  }
}

export default AppContainer
