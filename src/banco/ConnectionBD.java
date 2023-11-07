/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author lucasantonio-fit
 */
public class ConnectionBD {

    public static Connection FazerConeccao() {
        Connection conectar = null;

        String USER = "";
        String PASS = "";
        String SERVIDOR = "";
        String URL = "jdbc:mysql://" + SERVIDOR + ":3306/finger_app_bd";

        try {
            conectar = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            conectar = null;
        }
        return conectar;
    }
}

/*SQL

CREATE SCHEMA `finger_app_bd` DEFAULT CHARACTER SET utf8;
use finger_app_bd;

--
-- Banco de dados: `finger_app_bd`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `colaborador`
--

CREATE TABLE `colaborador` (
  `id` int(11) NOT NULL,
  `id_dados` int(11) NOT NULL,
  `id_digital` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='colaboradores\n  registrados';

-- --------------------------------------------------------

--
-- Estrutura para tabela `dados`
--

CREATE TABLE `dados` (
  `id` int(11) NOT NULL,
  `matricula` varchar(15) NOT NULL,
  `nome` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='base de\n  dados alimentada pelo rh';

--
-- Despejando dados para a tabela `dados`
--

INSERT INTO `dados` (`id`, `matricula`, `nome`) VALUES
(1, '12', 'testee1'),
(2, '45', 'testee2'),
(3, '51215', 'testee3'),
(4, '145645', 'testee4'),
(5, '120', 'testee5'),
(6, '1210', 'testee6');

-- --------------------------------------------------------

--
-- Estrutura para tabela `digital`
--

CREATE TABLE `digital` (
  `id` int(11) NOT NULL,
  `digital_1` varchar(1000) DEFAULT NULL,
  `digital_2` varchar(1000) DEFAULT NULL,
  `digital_3` varchar(1000) DEFAULT NULL,
  `digital_4` varchar(1000) DEFAULT NULL,
  `digital_5` varchar(1000) DEFAULT NULL,
  `digital_6` varchar(1000) DEFAULT NULL,
  `digital_7` varchar(1000) DEFAULT NULL,
  `digital_8` varchar(1000) DEFAULT NULL,
  `digital_9` varchar(1000) DEFAULT NULL,
  `digital_10` varchar(1000) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='digitais dos colaboradores';

-- --------------------------------------------------------

--
-- Estrutura para tabela `entrega`
--

CREATE TABLE `entrega` (
  `id` int(11) NOT NULL,
  `id_colaborador` int(11) NOT NULL,
  `data` datetime NOT NULL DEFAULT current_timestamp(),
  `justificativa` varchar(300) DEFAULT '',
  `id_item_entregue` varchar(100) DEFAULT 'kit minuano'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='confirmação de entrega';

-- --------------------------------------------------------

--
-- Estrutura para tabela `habilitado_a_receber`
--

CREATE TABLE `habilitado_a_receber` (
  `id` int(11) NOT NULL,
  `id_dados` int(11) NOT NULL,
  `id_item` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Despejando dados para a tabela `habilitado_a_receber`
--

INSERT INTO `habilitado_a_receber` (`id`, `id_dados`, `id_item`) VALUES
(1, 2, 2),
(2, 3, 3),
(3, 3, 4),
(4, 5, 5),
(5, 5, 6),
(6, 5, 7);

-- --------------------------------------------------------

--
-- Estrutura para tabela `item_entregue`
--

CREATE TABLE `item_entregue` (
  `id` int(11) NOT NULL,
  `item_nome` varchar(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Despejando dados para a tabela `item_entregue`
--

INSERT INTO `item_entregue` (`id`, `item_nome`) VALUES
(1, 'itemn1'),
(2, 'itemn2'),
(3, 'itens'),
(4, 'item'),
(5, 'algo'),
(6, 'algo2'),
(7, 'algo3');

-- --------------------------------------------------------

--
-- Estrutura para tabela `ocorrencias`
--

CREATE TABLE `ocorrencias` (
  `id` int(11) NOT NULL,
  `nome` varchar(60) DEFAULT NULL,
  `data` datetime DEFAULT current_timestamp(),
  `liberado` varchar(100) DEFAULT NULL,
  `justificativa` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Despejando dados para a tabela `ocorrencias`
--

INSERT INTO `ocorrencias` (`id`, `nome`, `data`, `liberado`, `justificativa`) VALUES
(1, 'lukas', '2023-11-05 14:45:24', 'sim', 'liberado'),
(2, 'lukas', '2023-11-05 15:33:12', 'colab', 'liberado'),
(3, 'lukas', '2023-11-05 15:34:11', 'colaborador em negado', 'liberado');

-- --------------------------------------------------------

--
-- Estrutura para tabela `sons`
--

CREATE TABLE `sons` (
  `id` int(11) NOT NULL,
  `nome` varchar(45) NOT NULL,
  `arquivo` mediumblob NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `colaborador`
--
ALTER TABLE `colaborador`
  ADD PRIMARY KEY (`id`,`id_digital`,`id_dados`);

--
-- Índices de tabela `dados`
--
ALTER TABLE `dados`
  ADD PRIMARY KEY (`id`,`matricula`);

--
-- Índices de tabela `digital`
--
ALTER TABLE `digital`
  ADD PRIMARY KEY (`id`);

--
-- Índices de tabela `entrega`
--
ALTER TABLE `entrega`
  ADD PRIMARY KEY (`id`);

--
-- Índices de tabela `habilitado_a_receber`
--
ALTER TABLE `habilitado_a_receber`
  ADD PRIMARY KEY (`id`);

--
-- Índices de tabela `item_entregue`
--
ALTER TABLE `item_entregue`
  ADD PRIMARY KEY (`id`,`item_nome`);

--
-- Índices de tabela `ocorrencias`
--
ALTER TABLE `ocorrencias`
  ADD PRIMARY KEY (`id`);

--
-- Índices de tabela `sons`
--
ALTER TABLE `sons`
  ADD PRIMARY KEY (`id`,`nome`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `colaborador`
--
ALTER TABLE `colaborador`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `dados`
--
ALTER TABLE `dados`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de tabela `digital`
--
ALTER TABLE `digital`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `entrega`
--
ALTER TABLE `entrega`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `habilitado_a_receber`
--
ALTER TABLE `habilitado_a_receber`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de tabela `item_entregue`
--
ALTER TABLE `item_entregue`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de tabela `ocorrencias`
--
ALTER TABLE `ocorrencias`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de tabela `sons`
--
ALTER TABLE `sons`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
COMMIT;


*/