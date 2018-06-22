Qt5 OpenEmbedded/Yocto Project layer
====================================

This layer depends on:

URI: https://github.com/openembedded/openembedded-core
branch: master
revision: HEAD

When building stuff like `qtdeclarative`, `qtquick`, `qtwebkit`, make
sure that you have required `PACKAGECONFIG` options enabled in qtbase
build, see `qtbase` recipe for detail.


Contributing
------------

Please submit any patches against the `meta-qt5` layer by using the
GitHub pull-request feature.  Fork the repo, make a branch, do the
work, rebase from upstream, create the pull request, yada-yada.

Maintainers
-----------

- Martin 'JaMa' Jansa <martin.jansa@gmail.com>
- Otavio Salvador <otavio@ossystems.com.br>

Yocto Project Compatible
------------------------

meta-qt5 has Yocto Project Compatible status since 2013:
https://www.yoctoproject.org/product/meta-qt5
