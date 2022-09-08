import { cilPlus, cilTrash } from '@coreui/icons';
import CIcon from '@coreui/icons-react';
import { Button, Col, Form, Input, notification, Radio, Row, Select, Space, Spin, Switch, Upload } from 'antd';
import queryString from 'query-string';
import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import authorApi from '../../api/authorApi';
import categoryApi from '../../api/categoryApi';
import productApi from '../../api/productApi';
import publisherApi from '../../api/publisherApi';

const ProductForm = (props) => {
    const { Option } = Select;
    const location = useLocation();

    const [isLoading, setIsLoading] = useState(false)
    const [categories, setCategories] = useState([])
    const [authors, setAuthors] = useState([])
    const [publishers, setPublishers] = useState([])

    const [form] = Form.useForm()

    const handleSubmit = (data) => {
        // setIsLoading(true)
        console.log(fileList);
        console.log(fileListDetail)
        console.log(data);
        const id = queryString.parse(location.search).id
        if (location.pathname === '/product/update') {
            const listImage = fileListDetail.map(f => f.originFileObj).filter(f => !!f)
            const images = fileListDetail.filter(f => f.path)
                .map(f => {
                    return {
                        id: f.uid,
                        path: f.path,
                        book: id
                    }
                })
            const formData = new FormData()
            const book = { ...data, images }
            if (fileList[0].thumbnail) {
                book.thumbnail = fileList[0].thumbnail
            } else {
                formData.append("thumbnail", fileList[0].originFileObj)
            }

            formData.append("dto",
                new Blob([JSON.stringify(book)], { type: "application/json" }))


            for (const img of listImage) {
                formData.append("images", img)
            }

            console.log(book)
            console.log(formData)
            productApi.update(id, formData).then(res => {
                console.log(res);
                notification.info({
                    message: `Notification`,
                    description: 'Success',
                    placement: 'top',
                });
            }).catch(err => {
                console.log(err);
                const data = err.response.data
                if (data.code === 2000) {
                    form.setFields([
                        {
                            name: "name",
                            errors: [data.message]
                        }
                    ])
                }
                if (data.code === 2001) {
                    form.setFields([
                        {
                            name: "slug",
                            errors: [data.message]
                        }
                    ])
                }
            }).finally(() => {
                setIsLoading(false)
            })
        } else {
            const listImage = fileListDetail.map(f => f.originFileObj).filter(f => !!f)
            const publicIdImages = fileListDetail.filter(f => f.publicIdImages).map(f => f.publicIdImages)
            const formData = new FormData()
            formData.append("dto",
                new Blob([JSON.stringify({ ...data })], { type: "application/json" }))

            formData.append("thumbnail", fileList[0].originFileObj)

            for (const img of listImage) {
                formData.append("images", img)
            }

            productApi.add(formData).then(res => {
                console.log(res);
                notification.info({
                    message: `Notification`,
                    description: 'Success',
                    placement: 'top',
                });
                form.resetFields()
                setFileList([])
                setFileListDetail([])

            }).catch(err => {
                const data = err.response.data
                if (data.code === 2000) {
                    form.setFields([
                        {
                            name: "name",
                            errors: [data.message]
                        }
                    ])
                }
                if (data.code === 2001) {
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

        categoryApi.getAll().then(res => {
            setCategories(res)
        })

        authorApi.getAll().then(res => {
            setAuthors(res)
        })

        publisherApi.getAll().then(res => {
            setPublishers(res)
        })

        if (location.pathname === '/product/update') {
            const id = queryString.parse(location.search).id
            productApi.get(id).then(res => {
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
                        name: "length",
                        value: res.length
                    },
                    {
                        name: "height",
                        value: res.height
                    },
                    {
                        name: "slug",
                        value: res.slug
                    },
                    {
                        name: "price",
                        value: res.price
                    },
                    {
                        name: "discount",
                        value: res.discount
                    },
                    {
                        name: "discountPrice",
                        value: res.discountPrice
                    },
                    {
                        name: "inStock",
                        value: res.inStock
                    },
                    {
                        name: "status",
                        value: res.status
                    },
                    {
                        name: "totalPages",
                        value: res.totalPages
                    },
                    {
                        name: "publishingYear",
                        value: res.publishingYear
                    },
                    {
                        name: "description",
                        value: res.description
                    },
                    {
                        name: "publisher",
                        value: res.publisher.id
                    },
                    {
                        name: "width",
                        value: res.width
                    },
                    {
                        name: "category",
                        value: res.category.id
                    },
                    {
                        name: "authors",
                        value: res.authors.map(author => author.id)
                    },
                    {
                        name: "attributes",
                        value: res.attributes
                    }
                ])
                setFileList([{
                    uid: "-1",
                    name: "xxx.png",
                    status: "done",
                    thumbUrl: res.thumbnailUrl,
                    thumbnail: res.thumbnail
                }])
                const imageDetailList = res.images.map(img => ({
                    uid: img.id,
                    name: img.path,
                    status: "done",
                    thumbUrl: img.pathUrl,
                    path: img.path
                }))
                setFileListDetail(imageDetailList)
                // form.setFieldsValue({ attributes: res.attributes })
            }).catch(err => {
            })
        }

    }, [location.pathname])




    const createSlug = data => {
        let slug = data.target.value.toLowerCase();

        slug = slug.replace(/á|à|ả|ạ|ã|ă|ắ|ằ|ẳ|ẵ|ặ|â|ấ|ầ|ẩ|ẫ|ậ/gi, 'a');
        slug = slug.replace(/é|è|ẻ|ẽ|ẹ|ê|ế|ề|ể|ễ|ệ/gi, 'e');
        slug = slug.replace(/i|í|ì|ỉ|ĩ|ị/gi, 'i');
        slug = slug.replace(/ó|ò|ỏ|õ|ọ|ô|ố|ồ|ổ|ỗ|ộ|ơ|ớ|ờ|ở|ỡ|ợ/gi, 'o');
        slug = slug.replace(/ú|ù|ủ|ũ|ụ|ư|ứ|ừ|ử|ữ|ự/gi, 'u');
        slug = slug.replace(/ý|ỳ|ỷ|ỹ|ỵ/gi, 'y');
        slug = slug.replace(/đ/gi, 'd');
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

    const [fileList, setFileList] = useState([])
    const [fileListDetail, setFileListDetail] = useState([])

    const handleChangeUpload = ({ fileList: newFileList }) => {
        setFileList(newFileList);
    }

    const handleChangeUploadDetail = ({ fileList: newFileListDetail }) => {
        setFileListDetail(newFileListDetail);
    }


    const searchAuthors = (input, option) => {
        return option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
    }



    return (
        <Spin spinning={isLoading}>
            <Form
                name="form"
                form={form}
                validateTrigger="onSubmit"
                labelCol={{ flex: '80px' }}
                labelAlign="left"
                labelWrap
                // wrapperCol={{ flex: 1 }}
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
                            message: "Name cannot be empty"
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
                    label="In Stock"
                    name="inStock"
                    initialValue={true}
                >
                    <Radio.Group>
                        <Radio value={true}>Yes</Radio>
                        <Radio value={false}>No</Radio>
                    </Radio.Group>
                </Form.Item>
                <Form.Item
                    label="Status"
                    name="status"
                    initialValue={true}
                >
                    <Radio.Group>
                        <Radio value={true}>Active</Radio>
                        <Radio value={false}>InActive</Radio>
                    </Radio.Group>
                </Form.Item>
                <Form.Item
                    label="Category"
                    name="category"
                    rules={[
                        {
                            required: true,
                            message: "Category cannot be empty"
                        }
                    ]}
                >
                    <Select
                        placeholder="Select category">
                        {
                            categories.map(parent => <Select.Option key={parent.id} value={parent.id}>
                                {parent.name}
                            </Select.Option>)
                        }
                    </Select>
                </Form.Item>
                <Form.Item
                    label="Price"
                    name="price"
                    rules={[
                        {
                            required: true,
                            message: "Price cannot be empty"
                        }
                    ]}
                >
                    <Input type={'number'} placeholder="Price" min={0} />
                </Form.Item>
                <Form.Item
                    label="Discount"
                    name="discount"
                    initialValue={false}
                >
                    <Radio.Group>
                        <Radio value={true}>Yes</Radio>
                        <Radio value={false}>No</Radio>
                    </Radio.Group>
                </Form.Item>

                <Form.Item
                    label="Discount Price"
                    name="discountPrice"
                >
                    <Input type={'number'} defaultValue={0} placeholder="Discount Price" min={0} />
                </Form.Item>
                <Row gutter={50}>
                    <Col span={8}>
                        <Form.Item
                            label="Length"
                            name="length"
                        >
                            <Input type={'number'} placeholder="Length" min={0} />
                        </Form.Item>
                    </Col>
                    <Col span={8}>
                        <Form.Item
                            label="Width"
                            name="width"
                        >
                            <Input type={'number'} placeholder="Width" min={0} />
                        </Form.Item>
                    </Col>
                    <Col span={8}>
                        <Form.Item
                            label="Height"
                            name="height"
                        >
                            <Input type={'number'} placeholder="Height" min={0} />
                        </Form.Item>
                    </Col>
                </Row>
                <Form.Item
                    label="Total Pages"
                    name="totalPages"
                >
                    <Input type={'number'} placeholder="Total Pages" min={0} />
                </Form.Item>
                <Form.Item
                    label="Publishing Year"
                    name="publishingYear"
                >
                    <Input placeholder="Publishing Year" />
                </Form.Item>
                <Form.Item
                    label="Description"
                    name="description"
                >
                    <Input placeholder="Description" />
                </Form.Item>
                <Form.Item label="Image" name="img" rules={[
                    {
                        validator(_, value) {
                            // const id = form.getFieldValue('id')
                            if (fileList.length === 0) {
                                return Promise.reject('Image is not uploaded');
                            }
                            return Promise.resolve()
                        }
                    }
                ]} >
                    <Upload
                        listType="picture-card"
                        fileList={fileList}
                        onChange={handleChangeUpload}
                        multiple={false}

                        beforeUpload={() => false}
                        showUploadList={{
                            showPreviewIcon: false
                        }}
                        accept="image/*"
                    >
                        {fileList.length < 1 && '+ Upload'}
                    </Upload>
                </Form.Item>

                <Form.Item
                    label="Image Details"
                    rules={[
                        {
                            validator(_, value) {
                                if (fileListDetail.length === 0) {
                                    return Promise.reject('Image details is not uploaded');
                                }
                                return Promise.resolve()
                            }
                        }
                    ]} >
                    <Upload
                        listType="picture-card"
                        fileList={fileListDetail}
                        onChange={handleChangeUploadDetail}
                        beforeUpload={() => false}
                        multiple={true}
                        accept="image/*"
                        showUploadList={{
                            showPreviewIcon: false
                        }}
                    >
                        {fileListDetail.length < 10 && '+ Upload'}
                    </Upload>
                </Form.Item>

                <Form.Item
                    label="Authors"
                    name="authors"
                    rules={[
                        {
                            required: true,
                            message: "Authors cannot be empty"
                        }
                    ]}
                >
                    <Select
                        mode="multiple"
                        style={{
                            width: '100%',
                        }}
                        placeholder="Select author"
                        filterOption={searchAuthors}
                    >
                        {
                            authors.map(item => <Option key={item.id} value={item.id}>{item.name}</Option>)
                        }
                    </Select>
                </Form.Item>
                <Form.Item
                    label="Publisher"
                    name="publisher"
                    rules={[
                        {
                            required: true,
                            message: "Publisher cannot be empty"
                        }
                    ]}
                >
                    <Select
                        style={{
                            width: '100%',
                        }}
                        placeholder="Select publisher"
                    >
                        {
                            publishers.map(item => <Option key={item.id} value={item.id}>{item.name}</Option>)
                        }
                    </Select>
                </Form.Item>

                <Form.Item
                    label="Attributes"
                    name="attributes">
                    <Form.List
                        name="attributes"
                    // rules={[
                    //     {
                    //         validator: async (_, names) => {
                    //             if (!names || names.length < 2) {
                    //                 return Promise.reject(new Error('At least 2 passengers'));
                    //             }
                    //         },
                    //     },
                    // ]}
                    >
                        {(fields, { add, remove }) => (
                            <>
                                {fields.map(({ key, name, ...restField }) => (
                                    <Space
                                        key={key}
                                        style={{
                                            display: 'flex',
                                            marginBottom: 8,
                                            flex: '1 1 1'
                                        }}
                                        align="baseline"
                                        className='product_attrs'
                                    >
                                        <Form.Item
                                            {...restField}
                                            name={[name, 'key']}
                                            rules={[
                                                {
                                                    required: true,
                                                    message: 'Missing key',
                                                },
                                            ]}
                                        >
                                            <Input placeholder="Key" />
                                        </Form.Item>
                                        <Form.Item
                                            {...restField}
                                            name={[name, 'value']}
                                            rules={[
                                                {
                                                    required: true,
                                                    message: 'Missing value',
                                                },
                                            ]}
                                        >
                                            <Input placeholder="Value" />
                                        </Form.Item>
                                        <CIcon icon={cilTrash} onClick={() => remove(name)} />
                                    </Space>
                                ))}
                                <Form.Item>
                                    <Button type="dashed" onClick={() => add()} block
                                        icon={<CIcon icon={cilPlus} />}>
                                        Add
                                    </Button>
                                </Form.Item>
                            </>
                        )}
                    </Form.List>
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit" disabled={isLoading}>
                        Lưu
                    </Button>
                </Form.Item>
            </Form>
        </Spin>
    )
}

export default ProductForm