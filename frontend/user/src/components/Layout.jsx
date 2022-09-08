import React from 'react'

import { BrowserRouter, Route } from 'react-router-dom'

import Footer from './Footer'
import Header from './Header'

import Routes from '../routes/Routes'
import { Category } from './Category'

const Layout = () => {
    return (
        <BrowserRouter>
            <Route render={props => (
                <div>
                    <Header {...props}/>
                    <div className="container">
                        <div className="main">
                            <Routes/>
                        </div>
                    </div>
                    <Footer/>
                    <Category />
                    {/* <Search /> */}
                </div>
            )}/>
        </BrowserRouter>
    )
}

export default Layout
