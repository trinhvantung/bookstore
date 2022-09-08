import { Table } from 'antd';
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import orderApi from '../api/orderApi';
import Helmet from '../components/Helmet';
import numberWithCommas from '../utils/numberWithCommas';

const columns = [
    {
        title: 'ID',
        dataIndex: 'id',
        key: 'id'
    },
    {
        title: 'Họ tên',
        dataIndex: 'fullName',
        key: 'fullName',
    },
    {
        title: 'Địa chỉ giao hàng',
        dataIndex: 'address',
        key: 'address',
    },
    {
        title: 'Tổng tiền',
        key: 'totalPrice',
        dataIndex: 'totalPrice',
        render: value => numberWithCommas(value) + " đ"
    },
    {
        title: 'Trạng thái',
        key: 'status',
        dataIndex: 'status'

    },
    {
        title: '',
        key: 'action',
        render: (_, order) => <Link to={`/order/${order.id}`}>Xem chi tiết</Link>,
    },
];




const Orders = () => {
    const [orders, setOrders] = useState([])
    const [isLoading, setIsLoading] = useState(false);
    const [response, setResponse] = useState({})

    useEffect(() => {
        load(1)
    }, [])

    const changePage = page => {
        console.log(page);
        load(page.current)
    }


    const load = page => {
        console.log(page)
        setIsLoading(true)
        orderApi.getAll({ page }).then(res => {
            setOrders(res.content)
            setIsLoading(false)
            setResponse(res)
        })
    }
    return (
        <Helmet title='Danh sách đơn hàng'>
            <Table rowKey={'id'} dataSource={orders} columns={columns} loading={isLoading}
                pagination={{ total: response.totalElements, showSizeChanger: false }}
                onChange={changePage} />;
        </Helmet>
    )
}

export default Orders