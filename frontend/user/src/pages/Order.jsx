import { Button, Form, Input, notification, Spin } from 'antd'
import React, { useEffect, useState } from 'react'
import { useHistory } from 'react-router-dom'
import cartApi from '../api/cartApi'
import orderApi from '../api/orderApi'
import Helmet from '../components/Helmet'
import OrderProductItem from '../components/OrderProductItem'
import numberWithCommas from '../utils/numberWithCommas'



const Order = () => {

    const [form] = Form.useForm()
    const isLoading = false
    const [totalPrice, setTotalPrice] = useState(0);
    const history = useHistory()

    const [cartItems, setCartItems] = useState([])

    const handleSubmit = (data) => {
        console.log(data);
        orderApi.create(data).then(res => {
            notification.info({
                message: `Notification`,
                description: 'Success',
                placement: 'top',
            });
            console.log(res);
            history.push("/orders")
        })
    }


    useEffect(() => {
        cartApi.getAll().then(res => {
            console.log(res);
            setCartItems(res)
        })

    }, [])

    useEffect(() => {
        setTotalPrice(cartItems.reduce((total, item) => {
            if (item.book.discount) {
                return total + (Number(item.quantity) * Number(item.book.discountPrice))
            } else {
                return total + (Number(item.quantity) * Number(item.book.price))
            }
        }, 0))
    }, [cartItems])

    

    return (
        <Helmet title='Đặt hàng'>
            <div className="order">
                <div className="order__list">
                    <Spin spinning={isLoading}>
                        <Form
                            name="form"
                            form={form}
                            validateTrigger="onSubmit"
                            labelCol={{ flex: '110px' }}
                            labelAlign="left"
                            labelWrap
                            wrapperCol={{ flex: 1 }}
                            autoComplete="off"
                            onFinish={handleSubmit}
                            requiredMark={false}
                            initialValues={{
                                id: '',
                                name: '',
                                image: null
                            }}
                        >
                            <Form.Item
                                label="Họ tên"
                                name="fullName"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Số điện thoại không được để trống',
                                    },
                                ]}
                            >
                                <Input placeholder="Họ tên" size='large' />
                            </Form.Item>

                            <Form.Item
                                label="Số điện thoại"
                                name="phoneNumber"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Số điện thoại không được để trống',
                                    },
                                ]}
                            >
                                <Input placeholder="Số điện thoại" size='large' />
                            </Form.Item>
                            <Form.Item
                                label="Địa chỉ"
                                name="address"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Địa chỉ không được để trống',
                                    },
                                ]}
                            >
                                <Input placeholder="Địa chỉ" size='large' />
                            </Form.Item>
                            <Form.Item
                                label="Ghi chú"
                                name="note"
                            >
                                <Input placeholder="Ghi chú" size='large' />
                            </Form.Item>

                            <Form.Item>
                                <Button size='large' type="primary" htmlType="submit" disabled={isLoading}>
                                    Đặt hàng
                                </Button>
                            </Form.Item>
                        </Form>
                    </Spin>
                </div>
                <div className="order__info">
                    <div className="order__info__txt">
                        {
                            cartItems.map((item, index) => (
                                <OrderProductItem key={index} data={item} />
                            ))
                        }
                        <div className="order__info__txt__price">
                            <span>Thành tiền:</span> <span>{numberWithCommas(totalPrice)} đ</span>
                        </div>
                    </div>
                </div>
            </div>
        </Helmet>
    )
}

export default Order
