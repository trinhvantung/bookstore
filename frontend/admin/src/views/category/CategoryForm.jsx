import { Button, Form, Input, notification, Select, Spin } from 'antd';
import queryString from 'query-string';
import { React, useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import categoryApi from '../../api/categoryApi';

const CategoryForm = (props) => {
    const location = useLocation();

    const [isLoading, setIsLoading] = useState(false)
    const [parents, setParents] = useState([])
    const [form] = Form.useForm()

    const handleSubmit = (data) => {
        console.log(data);
        if (data.parent == 0) {
            data.parent = null
        }
        console.log(data);
        setIsLoading(true)
        if (location.pathname === '/category/update') {
            const id = queryString.parse(location.search).id
            categoryApi.update(id, data).then(res => {
                console.log(res);
                notification.info({
                    message: `Notification`,
                    description: 'Success',
                    placement: 'top',
                });
            }).catch(err => {
                console.log(err);
                const data = err.response.data
                if (data.code === 4000) {
                    form.setFields([
                        {
                            name: "name",
                            errors: [data.message]
                        }
                    ])
                }
                if (data.code === 4001) {
                    form.setFields([
                        {
                            name: "slug",
                            errors: [data.message]
                        }
                    ])
                }
                console.log(data)
            }).finally(() => {
                setIsLoading(false)
            })
        } else {
            categoryApi.add(data).then(res => {
                console.log(res);
                notification.info({
                    message: `Notification`,
                    description: 'Success',
                    placement: 'top',
                });
                form.resetFields()

            }).catch(err => {
                const data = err.response.data
                if (data.code === 4000) {
                    form.setFields([
                        {
                            name: "name",
                            errors: [data.message]
                        }
                    ])
                }
                if (data.code === 4001) {
                    form.setFields([
                        {
                            name: "slug",
                            errors: [data.message]
                        }
                    ])
                }
                console.log(data)
            }).finally(() => {
                setIsLoading(false)
            })
        }
    }

    useEffect(() => {
        form.resetFields()

        categoryApi.getAllParent().then(res => {
            setParents(res)
        })

        if (location.pathname === '/category/update') {
            const id = queryString.parse(location.search).id
            categoryApi.get(id).then(res => {
                console.log(res);
                form.setFields([
                    {
                        name: "name",
                        value: res.name
                    },
                    {
                        name: "id",
                        value: res.id
                    },
                    {
                        name: "slug",
                        value: res.slug
                    }
                ])
                if (res.parent != null) {
                    form.setFields([
                        {
                            name: "parent",
                            value: res.parent.id
                        }
                    ])
                }
            }).catch(err => {
            })
        }

    }, [location.pathname])





    const createSlug = data => {
        let slug = data.target.value.toLowerCase();

        slug = slug.replace(/??|??|???|???|??|??|???|???|???|???|???|??|???|???|???|???|???/gi, 'a');
        slug = slug.replace(/??|??|???|???|???|??|???|???|???|???|???/gi, 'e');
        slug = slug.replace(/i|??|??|???|??|???/gi, 'i');
        slug = slug.replace(/??|??|???|??|???|??|???|???|???|???|???|??|???|???|???|???|???/gi, 'o');
        slug = slug.replace(/??|??|???|??|???|??|???|???|???|???|???/gi, 'u');
        slug = slug.replace(/??|???|???|???|???/gi, 'y');
        slug = slug.replace(/??/gi, 'd');
        slug = slug.replace(/`|~|!|@|#|\||\$|%|\^|&|\*|\(|\)|\+|=|,|\.|\/|\?|>|<|'|"|:|;|_/gi, '');
        slug = slug.replace(/ /gi, '-');
        slug = slug.replace(/-----/gi, '-');
        slug = slug.replace(/----/gi, '-');
        slug = slug.replace(/---/gi, '-');
        slug = slug.replace(/--/gi, '-');
        slug = '@' + slug + '@';

        slug = slug.replace(/@-|-@|@/gi, '');

        form.setFields([
            {
                name: "slug",
                value: slug
            }
        ])
    }

    return (
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
            >
                <Form.Item
                    label="ID"
                    name="id"
                >
                    <Input placeholder="ID" disabled />
                </Form.Item>
                <Form.Item
                    label="Name"
                    name="name"
                    rules={[
                        {
                            required: true,
                            message: "Category name cannot be empty"
                        }
                    ]}
                >
                    <Input placeholder="Name" onChange={data => createSlug(data)} />
                </Form.Item>
                <Form.Item
                    label="Slug"
                    name="slug"
                    rules={[
                        {
                            required: true,
                            message: "Slug cannot be empty"
                        }
                    ]}
                >
                    <Input placeholder="Slug" />
                </Form.Item>
                <Form.Item
                    label="Parent Category"
                    name="parent"
                    initialValue={0}
                >
                    <Select>
                        <Select.Option value={0}>None</Select.Option>
                        {
                            parents.map(parent => <Select.Option key={parent.id} value={parent.id}>
                                {parent.name}
                            </Select.Option>)
                        }
                    </Select>
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit" disabled={isLoading}>
                        L??u
                    </Button>
                </Form.Item>
            </Form>
        </Spin>
    )
}

export default CategoryForm